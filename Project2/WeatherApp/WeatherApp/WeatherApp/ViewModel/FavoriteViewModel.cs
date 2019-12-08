using System.Collections.ObjectModel;
using System.Windows.Input;
using WeatherApp.Models;
using Xamarin.Forms;

namespace WeatherApp.ViewModel
{
    public class FavoriteViewModel : ViewModelBase
    {
        public ObservableCollection<CityInfo> cities;
        public ObservableCollection<CityInfo> favoriteCities;
        public CityInfo selectedCity;


        public FavoriteViewModel()
        {
            FavoriteCities = new ObservableCollection<CityInfo>();
            Cities = CitiesData();
            AddCityCommand = new Command<CityInfo>(AddCity);
            RemoveCityCommand = new Command<CityInfo>(RemoveCity);
        }

        public ObservableCollection<CityInfo> Cities
        {
            get => cities;
            set => SetProperty(ref cities, value);
        }

        public ObservableCollection<CityInfo> FavoriteCities
        {
            get => favoriteCities;
            set => SetProperty(ref favoriteCities, value);
        }

        public CityInfo SelectedCity
        {
            get => selectedCity;
            set => SetProperty(ref selectedCity, value);
        }

        public ICommand AddCityCommand { get; }
        public ICommand RemoveCityCommand { get; }

        private ObservableCollection<CityInfo> CitiesData()
        {
            return new ObservableCollection<CityInfo>
            {
                new CityInfo("Porto"),
                new CityInfo("Lisboa"),
                new CityInfo("Coimbra")
            };
        }

        private async void AddCity(CityInfo selectedCity)
        {
            if (selectedCity == null)
                return;

            await selectedCity.OnLoadWeatherForecast();
            FavoriteCities.Add(selectedCity);
            Cities.Remove(selectedCity);
            this.selectedCity = Cities.Count > 0 ? Cities[0] : null;
        }

        private async void RemoveCity(CityInfo city)
        {
            FavoriteCities.Remove(city);
            Cities.Add(city);
        }
    }
}