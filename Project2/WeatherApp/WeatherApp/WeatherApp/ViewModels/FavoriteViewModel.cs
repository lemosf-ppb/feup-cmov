using System;
using System.Collections;
using System.Collections.ObjectModel;
using System.IO;
using System.Windows.Input;
using WeatherApp.Models;
using Xamarin.Forms;

namespace WeatherApp.ViewModels
{
    public class FavoriteViewModel : ViewModelBase
    {
        private ObservableCollection<CityInfo> _cities;
        private ObservableCollection<CityInfo> _favoriteCities;
        private CityInfo _selectedCity;
        private bool isLoading;

        public FavoriteViewModel()
        {
            FavoriteCities = new ObservableCollection<CityInfo>();
            Cities = CitiesData();
            AddCityCommand = new Command<CityInfo>(AddCity);
            RemoveCityCommand = new Command<CityInfo>(RemoveCity);
            SyncCommand = new Command(Sync);

            LoadData();
        }

        public bool IsLoading
        {
            get => isLoading;
            set => SetProperty(ref isLoading, value);
        }

        public ObservableCollection<CityInfo> Cities
        {
            get => _cities;
            set => SetProperty(ref _cities, value);
        }

        public ObservableCollection<CityInfo> FavoriteCities
        {
            get => _favoriteCities;
            set => SetProperty(ref _favoriteCities, value);
        }

        public CityInfo SelectedCity
        {
            get => _selectedCity;
            set => SetProperty(ref _selectedCity, value);
        }

        public ICommand AddCityCommand { get; }
        public ICommand RemoveCityCommand { get; }

        public ICommand SyncCommand { get; }

        private ObservableCollection<CityInfo> CitiesData()
        {
            return new ObservableCollection<CityInfo>
            {
                new CityInfo("Aveiro"),
                new CityInfo("Beja"),
                new CityInfo("Braga"),
                new CityInfo("Bragança"),
                new CityInfo("Castelo Branco"),
                new CityInfo("Coimbra"),
                new CityInfo("Évora"),
                new CityInfo("Faro"),
                new CityInfo("Guarda"),
                new CityInfo("Leiria"),
                new CityInfo("Lisboa"),
                new CityInfo("Portalegre"),
                new CityInfo("Porto"),
                new CityInfo("Santarém"),
                new CityInfo("Setúbal"),
                new CityInfo("Viana do Castelo"),
                new CityInfo("Vila Real"),
                new CityInfo("Viseu")
            };
        }

        private async void AddCity(CityInfo selectedCity)
        {
            if (selectedCity == null)
                return;
            IsLoading = true;
            await selectedCity.OnLoadWeatherForecast();
            IsLoading = false;

            FavoriteCities.Add(selectedCity);
            Cities.Remove(selectedCity);
            _selectedCity = Cities.Count > 0 ? Cities[0] : null;

            StoreData();
        }

        private static void BubbleSort(IList o)
        {
            for (var i = o.Count - 1; i >= 0; i--)
            for (var j = 1; j <= i; j++)
            {
                var o1 = o[j - 1];
                var o2 = o[j];
                if (((IComparable) o1).CompareTo(o2) <= 0) continue;

                o.Remove(o1);
                o.Insert(j, o1);
            }
        }

        private void RemoveCity(CityInfo city)
        {
            FavoriteCities.Remove(city);
            Cities.Add(city);
            BubbleSort(Cities);
            
            StoreData();
        }

        private async void Sync()
        {
            if (_favoriteCities.Count == 0) return;

            IsLoading = true;
            foreach (var city in _favoriteCities) await city.OnLoadWeatherForecast();

            IsLoading = false;
        }

        private void StoreData()
        {
            var filePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData),
                "data.txt");

            var data = "";
            for (var i = 0; i < _favoriteCities.Count; i++)
            {
                data += _favoriteCities[i].Name;
                if (i != _favoriteCities.Count - 1) data += "\n";
            }

            File.WriteAllText(filePath, data);
        }

        private void LoadData()
        {
            var filePath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData),
                "data.txt");

            if (!File.Exists(filePath)) return;

            string line;
            var file = new StreamReader(filePath);
            while ((line = file.ReadLine()) != null)
            {
                var cityInfo = new CityInfo(line);
                AddCity(cityInfo);
            }

            file.Close();
        }
    }
}