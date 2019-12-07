using System;
using System.Globalization;
using System.IO;
using System.Net;
using Xamarin.Forms;

namespace WeatherApp.Converters
{
    public class ImageSourceConverter : IValueConverter
    {
        static WebClient Client = new WebClient();

        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            var byteArray = Client.DownloadData(value.ToString());
            return ImageSource.FromStream(() => new MemoryStream(byteArray));
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}