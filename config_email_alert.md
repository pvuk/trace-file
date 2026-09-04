# 🔑 Step‑by‑Step: Create a Gmail App Password
1. Go to Security settings  
	
	You’re already on Google Account → Security (https://myaccount.google.com/security?utm_source=copilot.com).

2. Enable 2‑Step Verification (if not already enabled)

	Scroll down to “Signing in to Google”.
	
	Make sure 2‑Step Verification is turned ON.
	
	If it’s OFF, click it and follow the prompts to enable (you’ll need your phone for SMS or Google Authenticator).

3. Find “App passwords”

	Once 2‑Step Verification is enabled, you’ll see a new option called App passwords under “Signing in to Google”.
	
	Click App passwords.

4. Generate a new App Password

	Google will ask you to log in again for security.
	
	On the App Passwords page:
	
		Select App → Mail.
		
		Select Device → Windows Computer (or choose “Other” and type something like “Spring Boot”).
	
	Click Generate.

5. Copy the 16‑digit password

	Google will show you a 16‑character password (looks like abcd efgh ijkl mnop).
	
	Copy it carefully — this is your new SMTP password.

6. Update your Spring Boot config  
	
	In application.yml, replace your Gmail password with the App Password:
	
	yaml
	spring:
	  mail:
	    host: smtp.gmail.com
	    port: 587
	    username: your-email@gmail.com
	    password: abcd efgh ijkl mnop   # paste App Password here
	    properties:
	      mail.smtp.auth: true
	      mail.smtp.starttls.enable: true
	      mail.smtp.starttls.required: true
	      mail.smtp.ssl.trust: smtp.gmail.com

# 🚀 After this
	Restart your Spring Boot app.
	
	Gmail will now accept the connection because you’re using a valid App Password with STARTTLS enforced.

