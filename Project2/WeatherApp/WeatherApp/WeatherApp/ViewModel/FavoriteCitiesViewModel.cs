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

        public void AddCity()
        {
            if (_selectedCity == null)
            {
                return;
            }
            
            new WeatherList(_selectedCity, this);
        }
        
        public void RemoveCity(City city)
        {
            FavoriteCities.Remove(city);
            Cities.Add(city);
        }

        public void UpdateViewModel(City selectedCity)
        {
            FavoriteCities.Add(selectedCity);
            Cities.Remove(_selectedCity);
            _selectedCity = Cities.Count > 0 ? Cities[0] : null;
        }
    }
}