using System;
using System.Globalization;
using System.IO;
using System.Net;
using Xamarin.Forms;

namespace WeatherApp.Converters
{
    public class ImageSourceConverter : IValueConverter
    {
        static WebClient _client = new WebClient();

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var byteArray = _client.DownloadData($"http://openweathermap.org/img/wn/{value}@2x.png");
            return ImageSource.FromStream(() => new MemoryStream(byteArray));
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}