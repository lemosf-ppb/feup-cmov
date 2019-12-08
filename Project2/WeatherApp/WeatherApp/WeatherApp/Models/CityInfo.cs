using System;
using System.Collections.Generic;
using System.Globalization;
using System.Threading.Tasks;
using WeatherApp.Services;

namespace WeatherApp.Models
{
    public class CityInfo
    {
        public CityInfo(string name)
        {
            Name = name;
            WeatherForecast = new WeatherForecast();
        }

        public string Name { get; set; }
        public WeatherForecast WeatherForecast { get; set; }

        public WeatherNow WeatherNow { get; set; }

        public async Task OnLoadWeatherForecast()
        {
            WeatherForecast = await WeatherApi.GetWeatherForecast(Name);
            WeatherNow = await WeatherApi.GetWeatherNow(Name);
            OnCalculateWeatherByDays();
        }

        private void OnCalculateWeatherByDays()
        {
            var weatherByDays = new Dictionary<int, List<WeatherData>>();

            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherData>();

            foreach (var weather in WeatherForecast.Weather)
            {
                var date = DateTime.ParseExact(weather.DtTxt, "yyyy-MM-dd HH:mm:ss",
                    CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    weatherByDays[day] = weatherByHours;
                    day++;
                    if (day > 1) break;

                    weatherByHours = new List<WeatherData>();
                }

                previousHour = hour;
                weatherByHours.Add(weather);
            }

            WeatherForecast.WeatherByDays = weatherByDays;
        }
    }
}