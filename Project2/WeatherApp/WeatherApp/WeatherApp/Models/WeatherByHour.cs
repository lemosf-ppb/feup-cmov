using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Xamarin.Forms;

namespace WeatherApp.Models
{
    public class WeatherByHour
    {
        public int Dt;
        
        public double Temp;
        public double Temp_min;
        public double Temp_max;
        public int Pressure;
        public int Sea_level;
        public int Grnd_level;
        public int Humidity;
        public int Temp_kf;

        public int Id;
        public string MainStatus;
        public string Description;
        public string Icon;

        public int Cloudiness;

        public double WindSpeed;
        public int Deg;

        public int Rain;

        public string dt_txt;

        private ImageSource _iconSource;
        public ImageSource IconSource
        {
            get => _iconSource;
            private set => _iconSource = value;
        }

        public WeatherByHour(JToken item)
        {
            Dt = (int) item["dt"];
                
            var main = item["main"];
            Temp = ConvertToCelsius((double) main["temp"]);
            Temp_min = ConvertToCelsius((double) main["temp_min"]);
            Temp_max = ConvertToCelsius((double) main["temp_max"]);
            Pressure = (int) main["pressure"];
            Sea_level = (int) main["sea_level"];
            Grnd_level = (int) main["grnd_level"];
            Humidity = (int) main["humidity"];
            Temp_kf =  (int) main["temp_kf"];

            var weather = item["weather"][0];
            Id = (int) weather["id"];
            MainStatus = (string) weather["main"];
            Description = (string) weather["description"];
            Icon = (string) weather["icon"];

            var clouds = item["clouds"];
            Cloudiness = (int) clouds["all"];

            var wind = item["wind"];
            WindSpeed = (double) wind["speed"];
            Deg = (int) wind["deg"];

            var rain = item["rain"];
            if (rain != null)
            {
                Rain = (int) rain["3h"];
            }
            else
            {
                Rain = 0;
            }

            dt_txt = (string) item["dt_txt"];
            IconSource = GetImageSource();
        }

        private double ConvertToCelsius(double kelvin)
        {
            return  Math.Round(kelvin - 273.15, 1);
        }

        private ImageSource GetImageSource()
        {
            var client = new WebClient();
            var url = "http://openweathermap.org/img/wn/" + Icon + "@2x.png";
            var byteArray = client.DownloadData(url);
            return ImageSource.FromStream(() => new MemoryStream(byteArray));
        }
    }
}