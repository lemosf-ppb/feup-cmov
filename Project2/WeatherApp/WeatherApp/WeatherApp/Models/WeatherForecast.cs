using System.Collections.Generic;
using Newtonsoft.Json;
using WeatherApp.Services;
using Xamarin.Forms;

namespace WeatherApp.Models
{
    public class WeatherForecast
    {
        [JsonProperty("list")] public WeatherData[] Weather { get; set; }

        [JsonProperty("city")] public City City { get; set; }

        public Dictionary<int, List<WeatherData>> WeatherByDays { get; set; }
    }

    public class City
    {
        [JsonProperty("name")] public string Name { get; set; }

        [JsonProperty("coord")] public Coord Coord { get; set; }

        [JsonProperty("country")] public string Country { get; set; }

        [JsonProperty("sunrise")] public long Sunrise { get; set; }

        [JsonProperty("sunset")] public long Sunset { get; set; }
    }

    public class WeatherData
    {
        [JsonProperty("main")] public Main Main { get; set; }

        [JsonProperty("weather")] public Weather[] Weather { get; set; }

        [JsonProperty("clouds")] public Clouds Clouds { get; set; }

        [JsonProperty("wind")] public Wind Wind { get; set; }

        [JsonProperty("rain")] public Rain Rain { get; set; }

        [JsonProperty("dt")] public long Dt { get; set; }

        [JsonProperty("dt_txt")] public string DtTxt { get; set; }
    }


    public class Clouds
    {
        [JsonProperty("all")] public long All { get; set; }
    }

    public class Coord
    {
        [JsonProperty("lon")] public double Lon { get; set; }

        [JsonProperty("lat")] public double Lat { get; set; }
    }

    public class Main
    {
        [JsonProperty("temp")] public double Temperature { get; set; }

        [JsonProperty("temp_min")] public double TempMin { get; set; }

        [JsonProperty("temp_max")] public double TempMax { get; set; }

        [JsonProperty("pressure")] public long Pressure { get; set; }

        [JsonProperty("humidity")] public long Humidity { get; set; }
    }

    public class Weather
    {
        [JsonProperty("id")] public long Id { get; set; }

        [JsonProperty("main")] public string Visibility { get; set; }

        [JsonProperty("description")] public string Description { get; set; }

        [JsonProperty("icon")] public string Icon { get; set; }

        public ImageSource IconSource => WeatherApi.GetIconSource(Icon);
    }

    public class Wind
    {
        [JsonProperty("speed")] public double Speed { get; set; }

        [JsonProperty("deg")] public long Deg { get; set; }
    }

    public class Rain
    {
        [JsonProperty("3h")] public double Value { get; set; }
    }
}