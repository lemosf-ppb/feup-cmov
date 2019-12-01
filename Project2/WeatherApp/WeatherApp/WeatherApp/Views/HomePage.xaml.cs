using System;
using System.Collections.Generic;
using WeatherApp.Models;
using WeatherApp.ViewModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class HomePage : ContentPage
    {
        private FavoriteCitiesViewModel _favoriteCitiesViewModel;
        public HomePage()
        {
            InitializeComponent();
            this.BindingContext = _favoriteCitiesViewModel = new FavoriteCitiesViewModel();
        }

        private async void OnItemSelected(Object sender, ItemTappedEventArgs e)
        {
            var details = e.Item as City;
            await Navigation.PushAsync(new CityDetail(details));
        }

        private async void OnFollowBtnClicked(Object sender, EventArgs args)
        {
            _favoriteCitiesViewModel.addCity();
        }
        
        private async void OnUnFollowBtnClicked(Object sender, EventArgs args)
        {
            var button = sender as Button;
            var city = button?.BindingContext as City;
            _favoriteCitiesViewModel.removeCity(city);
        }
    }
}