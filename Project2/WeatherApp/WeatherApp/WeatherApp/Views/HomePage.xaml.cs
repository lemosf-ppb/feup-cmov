using WeatherApp.Models;
using WeatherApp.ViewModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class HomePage : ContentPage
    {
        public HomePage()
        {
            InitializeComponent();
            BindingContext = new FavoriteViewModel();
        }

        private async void OnItemSelected(object sender, ItemTappedEventArgs e)
        {
            var citySelected = e.Item as CityInfo;
            await Navigation.PushAsync(new CityDetail(citySelected));
        }
    }
}