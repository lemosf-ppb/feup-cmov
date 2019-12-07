using System;
using System.Collections.Generic;
using System.ComponentModel;
using Xamarin.Forms;

namespace WeatherApp.Models
{
    public class City : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        public string Name { get; set; }

        private Dictionary<int, List<WeatherByHour>> _weatherByDays;
        private double _temp;

        public Dictionary<int, List<WeatherByHour>> WeatherByDays
        {
            get => _weatherByDays;
            set
            {
                _weatherByDays = value;
                Temp = _weatherByDays[0][0].Temp;
                // Call OnPropertyChanged whenever the property is updated
                OnPropertyChanged("WeatherByDaysChanged");
            }
        }

        public double Temp
        {
            get => _temp;
            set
            {
                _temp = value;
                // Call OnPropertyChanged whenever the property is updated
                OnPropertyChanged("TempChanged");
            }
        }

        public ImageSource Icon => ImageSource.FromUri(new Uri("http://openweathermap.org/img/wn/"+ WeatherByDays[0][0].Icon + "@2x.png"));

        private void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}