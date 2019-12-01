using System;
using System.Globalization;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityTomorrow : ContentPage
    {
        public CityTomorrow(City city, DateTime tomorrow)
        {
            InitializeComponent();

            CityName.Text = city.Name;
            Date.Text = $"{tomorrow:dddd, MMMM d, yyyy}";
        }
        
        async void OnFollowBtnClicked(object sender, EventArgs args)
        {
            //Remove city from list
        }
    }
}