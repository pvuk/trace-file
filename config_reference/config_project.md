# Check config_email_alerts.md file also for email configuration.

# 1 Pick Username & Password from Environment Variables in application.yml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            
Then set environment variables in your system:

Linux/macOS:

bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
Windows (PowerShell):

powershell
setx MAIL_USERNAME "your-email@gmail.com"
setx MAIL_PASSWORD "your-app-password"
This way, credentials are not hardcoded.

🔎 Check Existing Environment Variables
1. Command Prompt (cmd)
cmd
echo %MAIL_USERNAME%
echo %MAIL_PASSWORD%
If the variable exists, it will print the value.

If not, it will just echo %MAIL_USERNAME%.

2. PowerShell
powershell
echo $env:MAIL_USERNAME
echo $env:MAIL_PASSWORD
3. System Environment Variables (GUI)
Press Win + R, type sysdm.cpl, press Enter.

Go to Advanced → Environment Variables.

Look under User variables or System variables for MAIL_USERNAME and MAIL_PASSWORD.

# ⚡ Difference Between set and setx
set → temporary, only for the current session (disappears when you close cmd).

setx → permanent, stored in registry, available in new sessions (but not in the current one immediately).

So after running:

powershell
setx MAIL_USERNAME "your-email@gmail.com"
setx MAIL_PASSWORD "your-app-password"
You need to open a new terminal (or log out/in) to see them.
