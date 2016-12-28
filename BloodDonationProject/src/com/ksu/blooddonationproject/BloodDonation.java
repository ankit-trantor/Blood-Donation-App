package com.ksu.blooddonationproject;

public class BloodDonation {
	private String mPatientName, mCityName, mHospitalName, mPatientFileNum, mObjectId, mOrderId;
	private int mRemainingNumber;

	public String getPatientName() {
		return mPatientName;
	}

	public void setPatientName(String patientName) {
		this.mPatientName = patientName;
	}

	// =================================
	public String getCityName() {
		return mCityName;
	}

	public void setCityName(String cityName) {
		this.mCityName = cityName;
	}

	// =================================
	public String getHospitalName() {
		return mHospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.mHospitalName = hospitalName;
	}

	// =================================
	public String getPatientFileNum() {
		return mPatientFileNum;
	}

	public void setPatientFileNum(String patientFileNum) {
		this.mPatientFileNum = patientFileNum;
	}

	// =================================
	public String getObjectId() {
		return mObjectId;
	}

	public void setObjectId(String objectId) {
		this.mObjectId = objectId;
	}

	// =================================
	public String getOrderId() {
		return mOrderId;
	}

	public void setOrderId(String orderId) {
		this.mOrderId = orderId;
	}

	// =================================
	public int getRemainingNumber() {
		return mRemainingNumber;
	}

	public void setRemainingNumber(int remainingNumber) {
		this.mRemainingNumber = remainingNumber;
	}

}