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
        public CityDetail(City city)
        {
            InitializeComponent();

            this.City = city;
            CityName.Text = city.Name;

            Today = DateTime.Now;
            
            Date.Text = $"{Today:dddd, MMMM d, yyyy}";
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