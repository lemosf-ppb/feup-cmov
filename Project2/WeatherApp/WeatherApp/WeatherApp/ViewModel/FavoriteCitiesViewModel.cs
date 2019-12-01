using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using WeatherApp.Models;

namespace WeatherApp.ViewModel
{
    public class FavoriteCitiesViewModel
    {
        public ObservableCollection<City> Cities { get; set; }
        public ObservableCollection<City> FavoriteCities { get; set; }
        private City _selectedCity { get; set; }

        public FavoriteCitiesViewModel()
        {
            FavoriteCities = new ObservableCollection<City>();
            Cities = CitiesData();
        }
        
        private ObservableCollection<City> CitiesData()
        {
            var cities = new ObservableCollection<City>();
            cities.Add(new City { Name = "Porto"});
            cities.Add(new City { Name = "Lisboa"});
            cities.Add(new City { Name = "Coimbra"});
            return cities;
        }
        
        public City SelectedCity
        {
            get => _selectedCity;
            set
            {
                if (_selectedCity != value)
                {
                    _selectedCity = value;
                }
            }
        }

        public void addCity()
        {
            if (_selectedCity == null)
            {
                return;
            }
            FavoriteCities.Add(_selectedCity);
            updateViewModel(_selectedCity);
        }

        public void removeCity(City city)
        {
            FavoriteCities.Remove(city);
            Cities.Add(city);
        }

        private void updateViewModel(City selectedCity)
        {
            Cities.Remove(_selectedCity);
            _selectedCity = Cities.Count > 0 ? Cities[0] : null;
        }
    }
}