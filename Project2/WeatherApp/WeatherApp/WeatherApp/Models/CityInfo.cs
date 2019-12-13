using System;
using System.Collections.Generic;
using System.Globalization;
using System.Threading.Tasks;
using WeatherApp.Services;

namespace WeatherApp.Models
{
    public class CityInfo : IComparable
    {
        public CityInfo(string name)
        {
            Name = name;
            WeatherForecast = new WeatherForecast();
        }

        public string Name { get; set; }
        public WeatherForecast WeatherForecast { get; set; }

        public WeatherNow WeatherNow { get; set; }

        public int CompareTo(object obj)
        {
            var secondObject = (CityInfo) obj;
            return string.CompareOrdinal(Name, secondObject.Name);
        }

        public async Task OnLoadWeatherForecast()
        {
            WeatherForecast = await WeatherApi.GetWeatherForecast(Name);
            WeatherNow = await WeatherApi.GetWeatherNow(Name);
            //WeatherNow.Weather[0].IconSource = WeatherApi.GetIconSource(WeatherNow.Weather[0].Icon);
            //WeatherNow.Weather[0].IconBitmap = WeatherApi.GetIconBitMap(WeatherNow.Weather[0].Icon);
            OnCalculateWeatherByDays();
        }

        private void OnCalculateWeatherByDays()
        {
            var weatherByDays = new Dictionary<int, List<WeatherData>>();

            var previousHour = 0;
            var day = 0;
            var weatherByHours = new List<WeatherData>();

            var dayAfterTomorrow = false;

            foreach (var weather in WeatherForecast.Weather)
            {
                var date = DateTime.ParseExact(weather.DtTxt, "yyyy-MM-dd HH:mm:ss",
                    CultureInfo.InvariantCulture);

                var hour = date.Hour;
                if (hour < previousHour)
                {
                    weatherByDays[day] = weatherByHours;
                    day++;

                    if (day > 1) dayAfterTomorrow = true;

                    weatherByHours = new List<WeatherData>();
                }

                previousHour = hour;

                //weather.Weather[0].IconSource = WeatherApi.GetIconSource(weather.Weather[0].Icon);
                //weather.Weather[0].IconBitmap = WeatherApi.GetIconBitMap(weather.Weather[0].Icon);
                weatherByHours.Add(weather);
                if (dayAfterTomorrow)
                {
                    weatherByDays[day] = weatherByHours;
                    break;
                }
            }

            WeatherForecast.WeatherByDays = weatherByDays;
        }

        public override bool Equals(Object obj)
        {
            //Check for null and compare run-time types.
            if ((obj == null) || !this.GetType().Equals(obj.GetType()))
            {
                return false;
            }
            else
            {
                CityInfo p = (CityInfo)obj;
                return Name == p.Name;
            }
        }
    }
}