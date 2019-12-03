using System;
using System.Collections.Generic;
using System.Globalization;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityTomorrow : ContentPage
    {
        private List<WeatherByHour> WeatherByHours { get; set; }
        public CityTomorrow(City city, DateTime tomorrow)
        {
            InitializeComponent();

            CityName.Text = city.Name;
            Date.Text = $"{tomorrow:dddd, MMMM d, yyyy}";
            
            WeatherByHours = city.WeatherList.WeatherByDays[1];
            
            Temperature.Text = WeatherByHours[0].Temp.ToString(CultureInfo.InvariantCulture);

            var i = 0;
            foreach (var weather in WeatherByHours)
            {
                var name = "Temp" + i;
                if (this.FindByName(name) is Label tempLabel)
                {
                    tempLabel.Text = weather.Temp.ToString(CultureInfo.InvariantCulture) + "°C";
                }

                i++;
            }
        }
        
        async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }
    }
}