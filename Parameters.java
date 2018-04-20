package application;

public class Parameters {

	String Time_Date;
	String fsk_rssi;
	String voltage;
	String current;
	String altitude;
	String latitude;
	String longitude;
	String tas;
	String gps_used;
	String fix_type;
	String satellites_num;
	String roll;
	String yaw;
	String pitch;
	String motor_status;
	String imu_status;
	String press_compass_status;
	String f_mode;
	String gps_status;
	String vehicle_type;
	String error_flags1;
	String gps_accH;

	public Parameters(String Time_Date, String fsk_rssi, String voltage, String current, String altitude, String latitude,
			String longitude, String tas, String gps_used, String fix_type, String satellites_num, String roll,
			String yaw, String pitch, String motor_status, String imu_status, String press_compass_status,
			String f_mode, String gps_status, String vehicle_type, String error_flags1, String gps_accH) {
		this.Time_Date = Time_Date;
		this.fsk_rssi = fsk_rssi;
		this.voltage = voltage;
		this.current = current;
		this.altitude = altitude;
		this.latitude = latitude;
		this.longitude = longitude;
		this.tas = tas;
		this.gps_status = gps_status;
		this.fix_type = fix_type;
		this.satellites_num = satellites_num;
		this.roll = roll;
		this.yaw = yaw;
		this.pitch = pitch;
		this.motor_status = motor_status;
		this.imu_status = imu_status;
		this.press_compass_status = press_compass_status;
		this.f_mode = f_mode;
		this.gps_status = gps_status;
		this.vehicle_type = vehicle_type;
		this.error_flags1 = error_flags1;
		this.gps_accH = gps_accH;

	}

	public String getTime_Date() {
		return Time_Date;
	}

	public void setTime_Date(String time_Date) {
		Time_Date = time_Date;
	}

	public String getFsk_rssi() {
		return fsk_rssi;
	}

	public void setFsk_rssi(String fsk_rssi) {
		this.fsk_rssi = fsk_rssi;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getAltitude() {
		return altitude;
	}

	public void setAltitude(String altitude) {
		this.altitude = altitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTas() {
		return tas;
	}

	public void setTas(String tas) {
		this.tas = tas;
	}

	public String getGps_used() {
		return gps_used;
	}

	public void setGps_used(String gps_used) {
		this.gps_used = gps_used;
	}

	public String getFix_type() {
		return fix_type;
	}

	public void setFix_type(String fix_type) {
		this.fix_type = fix_type;
	}

	public String getSatellites_num() {
		return satellites_num;
	}

	public void setSatellites_num(String satellites_num) {
		this.satellites_num = satellites_num;
	}

	public String getRoll() {
		return roll;
	}

	public void setRoll(String roll) {
		this.roll = roll;
	}

	public String getYaw() {
		return yaw;
	}

	public void setYaw(String yaw) {
		this.yaw = yaw;
	}

	public String getPitch() {
		return pitch;
	}

	public void setPitch(String pitch) {
		this.pitch = pitch;
	}

	public String getMotor_status() {
		return motor_status;
	}

	public void setMotor_status(String motor_status) {
		this.motor_status = motor_status;
	}

	public String getImu_status() {
		return imu_status;
	}

	public void setImu_status(String imu_status) {
		this.imu_status = imu_status;
	}

	public String getPress_compass_status() {
		return press_compass_status;
	}

	public void setPress_compass_status(String press_compass_status) {
		this.press_compass_status = press_compass_status;
	}

	public String getF_mode() {
		return f_mode;
	}

	public void setF_mode(String f_mode) {
		this.f_mode = f_mode;
	}

	public String getGps_status() {
		return gps_status;
	}

	public void setGps_status(String gps_status) {
		this.gps_status = gps_status;
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getError_flags1() {
		return error_flags1;
	}

	public void setError_flags1(String error_flags1) {
		this.error_flags1 = error_flags1;
	}

	public String getGps_accH() {
		return gps_accH;
	}

	public void setGps_accH(String gps_accH) {
		this.gps_accH = gps_accH;
	}
}
