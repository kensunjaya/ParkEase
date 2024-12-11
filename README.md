# **ParkEase**  
ParkEase is an innovative parking application designed to revolutionize the parking experience. Whether you're managing parking spaces or searching for one, ParkEase offers real-time updates, seamless functionality, and unparalleled convenience.  

The app integrates with an ESP32 microcontroller for sensor data collection via a REST API and leverages Firestore for robust database management, along with Firebase Authentication for secure user access.  

---

## **Key Features**  

- 📍 **Real-time Parking Availability**  
   Effortlessly view available parking spots and occupancy in real-time.  

- 🅿️ **Smart Booking System**  
   Reserve your parking space in advance to ensure a hassle-free experience.  

- 💳 **Integrated Payment Gateway**  
   Conveniently pay for parking directly within the app.  

- 📊 **Usage Analytics**  
   Gain valuable insights through a comprehensive dashboard for managing and monitoring parking usage.  

- 📱 **Mobile-Friendly Design**  
   A user-centric interface designed for intuitive navigation and ease of use.  

---

## **Getting Started**  

Follow the steps below to set up and run **ParkEase** on your local environment.  

### **Prerequisites**  
1. Install [Android Studio](https://developer.android.com/studio).  
2. Set up Kotlin with support for Jetpack Compose in your development environment.  
3. Configure your ESP32 microcontroller to send parking sensor data via a REST API.  
4. Set up **port forwarding** for your ESP32 or use TCP tunneling services like [ngrok](https://ngrok.com) or [playit.gg](https://playit.gg).  
5. Configure **Firebase Authentication** and **Firestore Database** in your Firebase project.  

### **Setup Instructions**  
1. **Clone the Repository**  
   Open a terminal and run the following commands:
   <br/>
   ```bash  
   git clone https://github.com/kensunjaya/ParkEase.git  
   cd ParkEase  
   ```
3. **Open in Android Studio**
    Launch Android Studio and select "Open an Existing Project."
    Navigate to the cloned repository and open the project.
