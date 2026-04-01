# Forgot Password Feature - Documentation

## Overview
This document describes the Forgot Password feature with OTP (One-Time Password) sent to Gmail.

## Features
1. **Send OTP Email**: User enters email and receives a 6-digit OTP
2. **Verify OTP**: User enters the OTP received in their email
3. **Reset Password**: User sets a new password after OTP verification
4. **Email Notifications**: Confirmation emails sent upon successful reset

## Backend Setup

### 1. Email Configuration
Configure your email settings in `application.properties`:

```properties
# Email Configuration (Gmail recommended)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
```

### 2. Gmail Setup
For Gmail:
1. Enable 2-Factor Authentication
2. Generate an App Password
3. Use the App Password in the configuration above

### Backend API Endpoints

#### 1. Request OTP
**POST** `/api/auth/forgot-password`

Request:
```json
{
  "email": "user@example.com"
}
```

Response (Success):
```json
{
  "message": "OTP has been sent to your email. Please check your inbox.",
  "success": true
}
```

#### 2. Verify OTP
**POST** `/api/auth/verify-otp`

Request:
```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

Response (Success):
```json
{
  "message": "OTP verified successfully.",
  "success": true
}
```

#### 3. Reset Password
**POST** `/api/auth/reset-password`

Request:
```json
{
  "email": "user@example.com",
  "otp": "123456",
  "newPassword": "newPassword123",
  "confirmPassword": "newPassword123"
}
```

Response (Success):
```json
{
  "message": "Password reset successfully. You can now log in with your new password.",
  "success": true
}
```

## Frontend Implementation

### Components
1. **ForgotPassword Page** (`src/pages/ForgotPassword.jsx`)
   - Multi-step form with Steps indicator
   - Step 1: Email entry
   - Step 2: OTP verification
   - Step 3: New password entry

### User Flow
1. User clicks "Forgot password?" link on Login page
2. Enters email and receives OTP
3. Verifies OTP in inbox
4. Sets new password
5. Redirected to Login page
6. Logs in with new password

### Frontend Routes
- `/forgot-password` - Forgot Password page

## Database Changes
The `User` entity has been updated with:
```java
private String resetOtp;           // Stores the OTP
private LocalDateTime resetOtpExpiry;  // OTP expiry time (10 minutes)
```

## Security Features
1. **OTP Expiry**: OTP is valid for 10 minutes only
2. **Password Requirements**: Minimum 6 characters
3. **Email Verification**: Only registered emails can reset password
4. **No Token Exposure**: OTP is not returned in API responses

## Error Handling
- Invalid email: "User not found with email"
- Expired OTP: "OTP has expired. Please request a new one."
- Invalid OTP: "Invalid OTP. Please enter the correct OTP."
- Password mismatch: "New password and confirm password do not match."

## Testing the Feature

### Manual Testing
1. Visit http://localhost:5173/forgot-password
2. Enter a registered email
3. Check email for OTP
4. Enter OTP
5. Set new password
6. Login with new password

### Test Credentials
Use any registered user email to test.

## Troubleshooting

### Emails not sending
- Check email configuration in `application.properties`
- Verify Gmail App Password is correct
- Ensure 2FA is enabled on Gmail
- Check Spring Boot logs for SMTP errors

### OTP not received
- Check email spam folder
- Verify email address is correct
- Check email server logs
- OTP expires after 10 minutes

### Password reset fails
- Ensure OTP hasn't expired
- Verify passwords match exactly
- Check password meets minimum length requirement

## Future Enhancements
1. Add SMS OTP support
2. Add OTP resend limit
3. Add password strength validation
4. Add rate limiting for OTP requests
5. Add email template customization
