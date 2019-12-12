using System;
using System.Collections.Generic;
using System.Globalization;
using SkiaSharp;
using SkiaSharp.Views.Forms;
using WeatherApp.Models;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityTomorrow : ContentPage
    {
        public CityTomorrow(CityInfo city, DateTime tomorrow)
        {
            InitializeComponent();

            CityName.Text = city.Name;
            Date.Text = $"{tomorrow:dddd, MMMM d, yyyy}";

            WeatherByHours = city.WeatherForecast.WeatherByDays[1];

            Temperature.Text = WeatherByHours[0].Main.Temperature.ToString(CultureInfo.InvariantCulture);
            IconSource.Source = WeatherByHours[0].Weather[0].IconSource;

            var i = 0;
            var minTemp = 100.0;
            var maxTemp = -100.0;

            var maxRain = 0.0;
            var minRain = 100.0;

            var maxHumidity = 0;
            var minHumidity = 100;

            var maxCloudiness = 0;
            var minCloudiness = 100;

            var maxPressure = 0.0;
            var minPressure = 10000.0;

            var maxWindSpeed = 0.0;
            var minWindSpeed = 10000.0;

            Entries = new List<GraphEntry>();

            foreach (var weather in WeatherByHours)
            {
                var name = "Temp" + i;
                if (FindByName(name) is Label tempLabel)
                {
                    var temp = weather.Main.Temperature;
                    var tempString = temp.ToString(CultureInfo.InvariantCulture) + "°C";
                    tempLabel.Text = tempString;

                    var date = weather.DtTxt;
                    var time = date.Substring(11, 2);

                    Entries.Add(new GraphEntry((float) temp, time + "h", tempString, weather.Weather[0].IconBitmap));
                }

                var icon = "Icon" + i;
                if (FindByName(icon) is Image iconImage) iconImage.Source = weather.Weather[0].IconSource;

                minTemp = UpdateMin(weather.Main.TempMin, minTemp);
                maxTemp = UpdateMax(weather.Main.TempMax, maxTemp);

                //WIND
                minWindSpeed = UpdateMin(weather.Wind.Speed, minWindSpeed);
                maxWindSpeed = UpdateMax(weather.Wind.Speed, maxWindSpeed);

                //PRESSURE
                minPressure = UpdateMin(weather.Main.Pressure, minPressure);
                maxPressure = UpdateMax(weather.Main.Pressure, maxPressure);

                //HUMIDITY
                minHumidity = (int) UpdateMin(weather.Main.Humidity, minHumidity);
                maxHumidity = (int) UpdateMax(weather.Main.Humidity, maxHumidity);

                //CLOUDINESS
                minCloudiness = (int) UpdateMin(weather.Clouds.All, minCloudiness);
                maxCloudiness = (int) UpdateMax(weather.Clouds.All, maxCloudiness);

                //PRECIPITATION
                var rainValue = 0.0;
                if (weather.Rain != null) rainValue = weather.Rain.Last3Hour;

                minRain = UpdateMin(rainValue, minRain);
                maxRain = UpdateMax(rainValue, maxRain);

                i++;
            }

            MaxMinCloudiness.Text = minCloudiness + "/" + maxCloudiness + "%";

            MaxMinHumidity.Text = minHumidity + "/" + maxHumidity + "%";

            MaxMinPrecipitation.Text = minRain + "/" + maxRain + "mm";

            MaxMinPressure.Text = minPressure + "/" + maxPressure + " hpa";

            MaxMinWind.Text = minWindSpeed + "/" + maxWindSpeed + " m/s";

            var dayAfterTomorrow = city.WeatherForecast.WeatherByDays[2][0];
            var dayAfterTomorrowTemp = dayAfterTomorrow.Main.Temperature;
            var dayAfterTomorrowTempString = dayAfterTomorrowTemp.ToString(CultureInfo.InvariantCulture) + "°C";

            Entries.Add(new GraphEntry((float) dayAfterTomorrowTemp, "00h", dayAfterTomorrowTempString,
                dayAfterTomorrow.Weather[0].IconBitmap));
            canvas.PaintSurface += OnPaint;
        }

        private List<GraphEntry> Entries { get; }

        private List<WeatherData> WeatherByHours { get; }

        private static double UpdateMin(double value, double min)
        {
            if (value < min) min = value;

            return min;
        }

        private static double UpdateMax(double value, double max)
        {
            if (value > max) max = value;

            return max;
        }

        private void OnPaint(object sender, SKPaintSurfaceEventArgs e)
        {
            var info = e.Info;
            var surface = e.Surface;
            var canvas = surface.Canvas;

            var maxWidth = (int) Math.Round(info.Width * 0.95);
            var maxHeight = (int) Math.Round(info.Height * 0.90);
            var minWidth = (int) Math.Round(info.Width * 0.15);
            var minHeight = (int) Math.Round(info.Height * 0.15);
            var circleRadius = (int) Math.Round((double) info.Width / 125);

            // clear canvas
            canvas.Clear(SKColor.Parse("#003B46"));

            var dotPaint = new SKPaint
            {
                IsAntialias = true,
                Color = SKColor.Parse("#C4DFE6"),
                StrokeCap = SKStrokeCap.Round,
                StrokeWidth = 12,
                TextSize = 30
            };

            var linePaint = new SKPaint
            {
                IsAntialias = true,
                Color = SKColor.Parse("#C4DFE6"),
                StrokeCap = SKStrokeCap.Round,
                StrokeWidth = 6,
                TextSize = 30
            };

            // calculate boundaries and rescale values
            var tempMax = double.MinValue;
            var tempMin = double.MaxValue;
            var hours = new List<string>();
            foreach (var t in Entries)
            {
                var temp = t.Temperature;
                hours.Add(t.Label);

                if (temp > tempMax)
                    tempMax = temp;
                if (temp < tempMin)
                    tempMin = temp;
            }

            var xStep = ((double) maxWidth - minWidth) / (Entries.Count - 1);
            tempMin = Entries.Count > 1 ? tempMax - 1.1 * (tempMax - tempMin) : tempMin;
            var yFactor = (maxHeight - minHeight) / (tempMax - tempMin);

            // draw graph axis and aux lines
            DrawGraphAxis(canvas, hours, new SKPoint(minWidth, minHeight), new SKPoint(maxWidth, maxHeight), xStep,
                tempMin, tempMax);

            // draw graph
            for (var i = 0; i < Entries.Count; i++)
            {
                double currentClose = Entries[i].Temperature;
                var currentX = (int) Math.Round(minWidth + i * xStep);
                var currentY = maxHeight - (int) Math.Round((currentClose - tempMin) * yFactor);

                var currentPoint = new SKPoint(currentX, currentY);
                canvas.DrawCircle(currentPoint, circleRadius, dotPaint);

                var iconBitMap = Entries[i].Icon;
                var width = iconBitMap.Width;
                var height = iconBitMap.Height;

                var iconPoint = new SKPoint(currentX - width / 2, currentY - height);

                canvas.DrawBitmap(iconBitMap, iconPoint, dotPaint);

                if (i == 0) continue;

                double prevClose = Entries[i - 1].Temperature;

                var prevY = maxHeight - (int) Math.Round((prevClose - tempMin) * yFactor);
                var prevX = (int) Math.Round(minWidth + (i - 1) * xStep);
                var prevPoint = new SKPoint(prevX, prevY);

                DrawShade(canvas, prevPoint, currentPoint, (float) tempMin, maxHeight);
                canvas.DrawLine(prevPoint, currentPoint, linePaint);
            }
        }

        private static void DrawShade(SKCanvas canvas, SKPoint prevPoint, SKPoint currentPoint, float minHeight,
            int maxHeight)
        {
            var x = (prevPoint.X + currentPoint.X) / 2;
            var strongColor = SKColor.Parse("#C4DFE6DA");
            var weakColor = SKColor.Parse("#C4DFE6BA");
            var shaderPaint = new SKPaint
            {
                Style = SKPaintStyle.StrokeAndFill,
                Shader = SKShader.CreateLinearGradient(
                    new SKPoint(x, minHeight),
                    new SKPoint(x, maxHeight),
                    new[]
                    {
                        strongColor,
                        weakColor
                    },
                    null,
                    SKShaderTileMode.Clamp)
            };
            var path = new SKPath {FillType = SKPathFillType.EvenOdd};
            path.MoveTo(prevPoint);
            path.LineTo(currentPoint);
            path.LineTo(currentPoint.X, maxHeight);
            path.LineTo(prevPoint.X, maxHeight);
            path.MoveTo(prevPoint);
            path.Close();
            canvas.DrawPath(path, shaderPaint);
        }

        private static void DrawGraphAxis(SKCanvas canvas, IReadOnlyList<string> hours, SKPoint min, SKPoint max,
            double xStep, double tempMin, double tempMax)
        {
            const int graphLines = 4;
            double heightDiff = max.Y - min.Y;
            var heightStep = heightDiff / graphLines;
            var tempStep = (tempMax - tempMin) / graphLines;

            var axisColor = SKColor.Parse("#66A5AD");

            var axisPaint = new SKPaint
            {
                Color = axisColor,
                StrokeCap = SKStrokeCap.Square,
                StrokeWidth = 10
            };

            var auxLinePaint = new SKPaint
            {
                Color = axisColor,
                StrokeCap = SKStrokeCap.Butt,
                StrokeWidth = 2
            };

            var textPaint = new SKPaint
            {
                Color = SKColor.Parse("#C4DFE6"),
                TextSize = 40
            };

            var origin = new SKPoint(min.X, max.Y);
            canvas.DrawLine(origin, new SKPoint(min.X, min.Y), axisPaint);
            canvas.DrawLine(origin, new SKPoint(max.X, max.Y), axisPaint);

            for (var i = 1; i <= graphLines; i++)
            {
                var height = (int) Math.Round(max.Y - i * heightStep);
                var temp = Math.Round(tempMin + i * tempStep, 1);
                canvas.DrawLine(new SKPoint(min.X, height), new SKPoint(max.X, height), auxLinePaint);
                canvas.DrawText(temp + "°C ", new SKPoint(0, height), textPaint);
            }


            double widthDiff = max.X - min.X;
            var widthOffset = widthDiff / 28;
            for (var i = 0; i < hours.Count; i++)
            {
                var width = (int) Math.Round(min.X + i * xStep);
                if (i != 0) canvas.DrawLine(new SKPoint(width, min.Y), new SKPoint(width, max.Y), auxLinePaint);

                canvas.DrawText(hours[i], new SKPoint(width - (float) widthOffset, min.Y + (float) heightDiff * 1.11f),
                    textPaint);
            }
        }
    }
}