package com.bhargavi.laxmi.sunshine.service.data;

/**
 * Created by laxmi on 6/26/15.
 */
public class WeatherResponse {
    private String cod ;
    private double message;
    private int cnt;
    private WeatherData[] list;

    public WeatherData[] getList() {
        return list;
    }

    public static class WeatherData {
        private long dt;
        private int humidity;
        private double pressure;
        private int clouds;
        private Temperature temp;
        private Weather[] weather;

        public long getDt() {
            return dt;
        }

        public Temperature getTemp() {
            return temp;
        }

        public Weather getWeather() {
            return weather[0];
        }

        public static class Temperature {
            private double min;
            private double max;

            public double getMin() {
                return min;
            }

            public double getMax() {
                return max;
            }
        }

        public static class Weather {
            private String description;
            private String main;
            private int id;
            private  String icon;

            public String getDescription() {
                return description;
            }

            public String getMain() {
                return main;
            }
        }

    }
}
