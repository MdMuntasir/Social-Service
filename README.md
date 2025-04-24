# Social Service System

A Java-based desktop application that facilitates connections between hirers, freelancers, and service heads in a social service marketplace.

## Features

- **For Hirers:**
  - Post new jobs with titles, descriptions, and rewards
  - Track posted jobs

- **For Freelancers:**
  - User registration and authentication
  - View available jobs
  - Request job assignments
  - Update profile information
  - Set availability status

- **For Service Heads:**
  - Administrative access to manage job assignments
  - View freelancer profiles
  - Assign jobs to qualified freelancers
  - Monitor job status

## Technical Stack

- Java Swing for the GUI
- Java HttpURLConnection for API communication
- JSON for data interchange
- Basic authentication for security

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven for dependency management
- A running instance of the Social Service backend server

## Setup

1. Clone the repository:
   ```bash
   git clone [your-repository-url]
   ```

2. Navigate to the project directory:
   ```bash
   cd social-service
   ```

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

4. Configure the backend URL:
   - Open `src/main/java/com/socialservice/client/SocialServiceClient.java`
   - Update the `BASE_URL` constant if your backend is running on a different URL
     (Default is `http://localhost:8080/SocialService/api`)

## Running the Application

1. Start the backend server first (refer to backend documentation)

2. Run the client application:
   ```bash
   java -jar target/social-service-client.jar
   ```

## Usage

### As a Hirer
1. Click on "Hirer" from the main menu
2. Fill in the job details:
   - Title
   - Description
   - Reward amount
3. Click "Post Job" to submit

### As a Freelancer
1. Click on "Freelancer" from the main menu
2. Register or login with your credentials
3. View available jobs
4. Request jobs by entering the job ID
5. Update your profile information as needed

### As a Service Head
1. Click on "Service Head" from the main menu
2. Login with administrative credentials
3. View all jobs and their status
4. Assign jobs to freelancers using job and freelancer IDs
5. View freelancer profiles for better assignment decisions

## Security

- The application uses Basic Authentication for API requests
- Passwords are transmitted securely
- Session management is handled through authentication headers
