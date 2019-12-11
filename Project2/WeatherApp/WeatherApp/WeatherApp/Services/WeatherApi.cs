using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using SkiaSharp;
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
                    var response =
                        await client.GetAsync(GenerateRequestUri(Constants.OpenWeatherMapForecastEndpoint, cityName));
                    if (response.IsSuccessStatusCode)
                    {
                        var content = await response.Content.ReadAsStringAsync();
                        Console.WriteLine(content);
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

        public static async Task<WeatherNow> GetWeatherNow(string cityName)
        {
            using (var client = new HttpClient())
            {
                WeatherNow weatherNow = null;
                try
                {
                    var response =
                        await client.GetAsync(GenerateRequestUri(Constants.OpenWeatherMapWeatherEndpoint, cityName));
                    if (response.IsSuccessStatusCode)
                    {
                        var content = await response.Content.ReadAsStringAsync();
                        weatherNow = JsonConvert.DeserializeObject<WeatherNow>(content);
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("\t\tERROR {0}", ex.Message);
                }

                return weatherNow;
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
        
        public static SKBitmap GetIconBitMap(string icon)
        {
            using (var client = new WebClient())
            {
                try
                {
                    var stream = client.OpenRead($"http://openweathermap.org/img/wn/{icon}@2x.png");
                    return SKBitmap.Decode(stream);
                }
                catch (Exception ex)
                {
                    Debug.WriteLine("\t\tERROR {0}", ex.Message);
                }

                return new SKBitmap();
            }
        }

        private static string GenerateRequestUri(string requestUri, string cityName)
        {
            requestUri += $"?q={cityName},PT";
            requestUri += "&units=metric";
            requestUri += $"&APPID={Constants.OpenWeatherMapApiKey}";
            return requestUri;
        }
    }
}