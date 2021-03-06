/*
 * 
 * PECoach
 * 
 * Copyright © 2009-2012 United States Government as represented by 
 * the Chief Information Officer of the National Center for Telehealth 
 * and Technology. All Rights Reserved.
 * 
 * Copyright © 2009-2012 Contributors. All Rights Reserved. 
 * 
 * THIS OPEN SOURCE AGREEMENT ("AGREEMENT") DEFINES THE RIGHTS OF USE, 
 * REPRODUCTION, DISTRIBUTION, MODIFICATION AND REDISTRIBUTION OF CERTAIN 
 * COMPUTER SOFTWARE ORIGINALLY RELEASED BY THE UNITED STATES GOVERNMENT 
 * AS REPRESENTED BY THE GOVERNMENT AGENCY LISTED BELOW ("GOVERNMENT AGENCY"). 
 * THE UNITED STATES GOVERNMENT, AS REPRESENTED BY GOVERNMENT AGENCY, IS AN 
 * INTENDED THIRD-PARTY BENEFICIARY OF ALL SUBSEQUENT DISTRIBUTIONS OR 
 * REDISTRIBUTIONS OF THE SUBJECT SOFTWARE. ANYONE WHO USES, REPRODUCES, 
 * DISTRIBUTES, MODIFIES OR REDISTRIBUTES THE SUBJECT SOFTWARE, AS DEFINED 
 * HEREIN, OR ANY PART THEREOF, IS, BY THAT ACTION, ACCEPTING IN FULL THE 
 * RESPONSIBILITIES AND OBLIGATIONS CONTAINED IN THIS AGREEMENT.
 * 
 * Government Agency: The National Center for Telehealth and Technology
 * Government Agency Original Software Designation: PECoach001
 * Government Agency Original Software Title: PECoach
 * User Registration Requested. Please send email 
 * with your contact information to: robert.kayl2@us.army.mil
 * Government Agency Point of Contact for Original Software: robert.kayl2@us.army.mil
 * 
 */
package org.t2health.pe;

import java.lang.reflect.Field;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

//Class disables the edittext's but there are other issues with the DatePickerDialog, so this is not used. - Steveo

public class AccessibleDatePicker extends DatePickerDialog {

	public AccessibleDatePicker(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fixPickerLayout();

	}

	public void fixPickerLayout() {
		// First, find the date picker object
		DatePicker picker = (DatePicker) getPrivateFieldValue(this,
				DatePickerDialog.class, "mDatePicker");
		if (picker == null)
			return;

		// Then, find the month picker object
		LinearLayout monthPicker = (LinearLayout) getPrivateFieldValue(picker,
				DatePicker.class, "mMonthPicker");
		if (monthPicker == null)
			return;

		// Fetch the month text field itself
		EditText monthTextField = (EditText) getPrivateFieldValue(monthPicker,
				monthPicker.getClass(), "mText");
		if (monthTextField == null)
			return;
		monthTextField.setEnabled(false);
		monthTextField.setFocusable(false);

		// Then, find the day picker object
		LinearLayout dayPicker = (LinearLayout) getPrivateFieldValue(picker,
				DatePicker.class, "mDayPicker");
		if (dayPicker == null)
			return;

		// Fetch the day text field itself
		EditText dayTextField = (EditText) getPrivateFieldValue(dayPicker,
				dayPicker.getClass(), "mText");
		if (dayTextField == null)
			return;
		dayTextField.setEnabled(false);
		dayTextField.setFocusable(false);

		// Then, find the year picker object
		LinearLayout yearPicker = (LinearLayout) getPrivateFieldValue(picker,
				DatePicker.class, "mYearPicker");
		if (yearPicker == null)
			return;

		// Fetch the year text field itself
		EditText yearTextField = (EditText) getPrivateFieldValue(yearPicker,
				yearPicker.getClass(), "mText");
		if (yearTextField == null)
			return;
		yearTextField.setEnabled(false);
		yearTextField.setFocusable(false);

	}

	/**
	 * @return the value of a private field in the given object; or null, if any
	 *         error occurs.
	 */
	public Object getPrivateFieldValue(Object object, Class<?> declaringClass,
			String fieldName) {
		try {
			// getDeclaredField retrieves private fields also, but doesn't
			// check other classes (makes sense!)
			Field d = declaringClass.getDeclaredField(fieldName);
			d.setAccessible(true);
			return d.get(object);
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}

}