# WebApp
## Server side of photo editor app

Tiny tutorial:
- This app uses https (port 8443)
- To sign up you should send POST-request to https://localhost:8443/users/signup with body:
{
  "username": "Dadya",
  "password": "Fedor",
  "email": "blabla@gmail.com"
}
- Then you need to prove your email to activate profile. You should go to email and click the link in the message.
- You can login now! You need to send POST-request to https://localhost:8443/login with the same body.
- Now you have a login token! 
