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
        public CityDetail(CityInfo cityInfo)
        {
            InitializeComponent();

            CityInfo = cityInfo;
            CityName.Text = cityInfo.Name;

            Today = DateTime.Now;

            Date.Text = $"{Today:dddd, MMMM d, yyyy}";

            var todayWeather = CityInfo.WeatherNow;
            Wind.Text = todayWeather.Wind.Speed.ToString(CultureInfo.InvariantCulture) + " m/s";

            Humidity.Text = todayWeather.Main.Humidity.ToString(CultureInfo.InvariantCulture) + "%";

            Pressure.Text = todayWeather.Main.Pressure.ToString(CultureInfo.InvariantCulture) + " hpa";

            Cloudiness.Text = todayWeather.Clouds.All.ToString(CultureInfo.InvariantCulture) + "%";

            var rainValue = 0.0;
            if (todayWeather.Rain != null) rainValue = todayWeather.Rain.LastHour;

            Precipitation.Text = rainValue.ToString(CultureInfo.InvariantCulture) + "mm";

            Temperature.Text = todayWeather.Main.Temperature.ToString(CultureInfo.InvariantCulture);

            IconSource.Source = todayWeather.Weather[0].IconSource;
        }

        public CityInfo CityInfo { get; set; }
        private DateTime Today { get; }

        private async void OnTomorrowBtnClicked(object sender, EventArgs args)
        {
            await Navigation.PushAsync(new CityTomorrow(CityInfo, Today.AddDays(1)));
        }
    }
}