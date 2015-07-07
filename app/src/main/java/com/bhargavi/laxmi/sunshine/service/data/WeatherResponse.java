package com.bhargavi.laxmi.sunshine.service.data;

import android.os.Parcel;
import android.os.Parcelable;

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

    public static class WeatherData implements Parcelable {
        private long dt;
        private int humidity;
        private double pressure;
        private int clouds;
        private Temperature temp;
        private Weather[] weather;
        private double speed;

        public long getDt() {
            return dt;
        }

        public int getHumidity(){return  humidity;}

        public double getPressure() {
            return pressure;
        }

        public Temperature getTemp() {
            return temp;
        }

        public Weather getWeather() {
            return weather[0];
        }

        public double getSpeed(){return speed;}

        public static class Temperature implements Parcelable {
            private double min;
            private double max;

            public double getMin() {
                return min;
            }

            public double getMax() {
                return max;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeDouble(this.min);
                dest.writeDouble(this.max);
            }

            public Temperature() {
            }

            protected Temperature(Parcel in) {
                this.min = in.readDouble();
                this.max = in.readDouble();
            }

            public static final Parcelable.Creator<Temperature> CREATOR = new Parcelable.Creator<Temperature>() {
                public Temperature createFromParcel(Parcel source) {
                    return new Temperature(source);
                }

                public Temperature[] newArray(int size) {
                    return new Temperature[size];
                }
            };
        }

        public static class Weather implements Parcelable {
            private String description;
            private String main;
            private int id;
            private  String icon;

            public int getId() {
                return id;
            }

            public String getDescription() {
                return description;
            }

            public String getMain() {
                return main;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.description);
                dest.writeString(this.main);
                dest.writeInt(this.id);
                dest.writeString(this.icon);
            }

            public Weather() {
            }

            protected Weather(Parcel in) {
                this.description = in.readString();
                this.main = in.readString();
                this.id = in.readInt();
                this.icon = in.readString();
            }

            public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
                public Weather createFromParcel(Parcel source) {
                    return new Weather(source);
                }

                public Weather[] newArray(int size) {
                    return new Weather[size];
                }
            };
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.dt);
            dest.writeInt(this.humidity);
            dest.writeDouble(this.pressure);
            dest.writeInt(this.clouds);
            dest.writeParcelable(this.temp, 0);
            dest.writeTypedArray(this.weather, flags);
            dest.writeDouble(speed);
        }

        public WeatherData() {
        }

        protected WeatherData(Parcel in) {
            this.dt = in.readLong();
            this.humidity = in.readInt();
            this.pressure = in.readDouble();
            this.clouds = in.readInt();
            this.temp = in.readParcelable(Temperature.class.getClassLoader());
            this.weather =  in.createTypedArray(Weather.CREATOR);
            this.speed = in.readDouble();
        }

        public static final Parcelable.Creator<WeatherData> CREATOR = new Parcelable.Creator<WeatherData>() {
            public WeatherData createFromParcel(Parcel source) {
                return new WeatherData(source);
            }

            public WeatherData[] newArray(int size) {
                return new WeatherData[size];
            }
        };
    }
}
