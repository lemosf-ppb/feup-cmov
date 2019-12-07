using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityDetail : ContentPage
    {
        private City City { get; set; }
        private DateTime Today { get; set; }
        
        private WeatherByHour TodayWeather { get; set; }
        public CityDetail(City city)
        {
            InitializeComponent();

            this.City = city;
            CityName.Text = city.Name;

            Today = DateTime.Now;
            
            Date.Text = $"{Today:dddd, MMMM d, yyyy}";

            TodayWeather = city.WeatherByDays[0][0];
            Wind.Text = TodayWeather.WindSpeed.ToString(CultureInfo.InvariantCulture) + " m/s";
            
            Humidity.Text = TodayWeather.Humidity.ToString(CultureInfo.InvariantCulture) + "%";
            
            Pressure.Text = TodayWeather.Pressure.ToString(CultureInfo.InvariantCulture) + " hpa";
            
            Cloudiness.Text = TodayWeather.Cloudiness.ToString(CultureInfo.InvariantCulture) + "%";
            
            Precipitation.Text = TodayWeather.Rain.ToString(CultureInfo.InvariantCulture) + "%";

            Temperature.Text = TodayWeather.Temp.ToString(CultureInfo.InvariantCulture);

        }
        
        async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }
        
        async void OnTomorrowBtnClicked(object sender, EventArgs args)
        {
            await Navigation.PushAsync(new CityTomorrow(City, Today.AddDays(1)));
        }
    }
    
    public class Weather
    {
        public string Temp { get; set; }
        public string Date { get; set; }
        public string Icon { get; set; }
    }
}