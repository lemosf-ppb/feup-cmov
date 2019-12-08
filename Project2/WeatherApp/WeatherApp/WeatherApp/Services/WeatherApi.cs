using System;
using System.Diagnostics;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using WeatherApp.Models;

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