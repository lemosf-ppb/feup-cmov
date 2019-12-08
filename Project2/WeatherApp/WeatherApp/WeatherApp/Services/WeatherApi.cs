using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using WeatherApp.Models;
using Xamarin.Forms;

namespace WeatherApp.Services
{
    public static class WeatherApi
    {
        public static async Task<WeatherForecast> GetWeatherForecast(string cityName)
        {
            using (var client = new HttpClient())
            {
                WeatherForecast weatherForecast = null;
                try
                {
                    var response = await client.GetAsync(GenerateRequestUri(cityName));
                    if (response.IsSuccessStatusCode)
                    {
                        var content = await response.Content.ReadAsStringAsync();
                        weatherForecast = JsonConvert.DeserializeObject<WeatherForecast>(content);
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("\t\tERROR {0}", ex.Message);
                }

                return weatherForecast;
            }
        }

        public static ImageSource GetIconSource(string icon)
        {
            using (var client = new WebClient())
            {
                try
                {
                    var byteArray = client.DownloadData($"http://openweathermap.org/img/wn/{icon}@2x.png");
                    return ImageSource.FromStream(() => new MemoryStream(byteArray));
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("\t\tERROR {0}", ex.Message);
                }

                return ImageSource.FromStream(() => new MemoryStream());
                ;
            }
        }

        private static string GenerateRequestUri(string cityName)
        {
            var requestUri = Constants.OpenWeatherMapEndpoint;
            requestUri += $"?q={cityName}";
            requestUri += "&units=metric";
            requestUri += $"&APPID={Constants.OpenWeatherMapApiKey}";
            return requestUri;
        }
    }
}