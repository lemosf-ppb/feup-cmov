using System;
using System.Globalization;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityDetail : ContentPage
    {
        public CityInfo CityInfo { get; set; }
        private DateTime Today { get; set; }

        public CityDetail(CityInfo cityInfo)
        {
            InitializeComponent();

            CityInfo = cityInfo;
            Console.WriteLine("oi" + CityInfo.Name);
            CityName.Text = cityInfo.Name;

            Today = DateTime.Now;

            Date.Text = $"{Today:dddd, MMMM d, yyyy}";

            var todayWeather = CityInfo.WeatherForecast.Weather[0];
            Wind.Text = todayWeather.Wind.Speed.ToString(CultureInfo.InvariantCulture) + " m/s";

            Humidity.Text = todayWeather.Main.Humidity.ToString(CultureInfo.InvariantCulture) + "%";

            Pressure.Text = todayWeather.Main.Pressure.ToString(CultureInfo.InvariantCulture) + " hpa";

            Cloudiness.Text = todayWeather.Clouds.All.ToString(CultureInfo.InvariantCulture) + "%";

            var rainValue = 0;
            if (todayWeather.Rain != null)
            {
                rainValue = (int) (todayWeather.Rain.Value * 100);
            }

            Precipitation.Text = rainValue.ToString(CultureInfo.InvariantCulture) + "%";

            Temperature.Text = todayWeather.Main.Temperature.ToString(CultureInfo.InvariantCulture);
        }

        private async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }

        private async void OnTomorrowBtnClicked(object sender, EventArgs args)
        {
            await Navigation.PushAsync(new CityTomorrow(CityInfo, Today.AddDays(1)));
        }
    }
}