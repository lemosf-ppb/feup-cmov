using System;
using Xamarin.Forms;

namespace WeatherApp.Models
{
    public class City
    {
        public string Name { get; set; }

        private WeatherList _weatherList { get; set; }
        
        public WeatherList WeatherList
        {
            get => _weatherList;
            set
            {
                if (_weatherList != value)
                {
                    _weatherList = value;
                }
            }
        }
        
        public double Temp => WeatherList.WeatherByDays[0][0].Temp;

        public ImageSource Icon => ImageSource.FromUri(new Uri("http://openweathermap.org/img/wn/"+ WeatherList.WeatherByDays[0][0].Icon + "@2x.png"));
    }
}