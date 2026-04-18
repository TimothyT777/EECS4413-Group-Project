# Project Clothing4413
An E-Commerce website that sells all your clothing needs.

## Requirements
The following is required:
- Docker Desktop
- Node.js is installed
- Git

## Steps to Build and Run

1. Clone the repository
   
    ```bash
    git clone https://github.com/TimothyT777/EECS4413-Group-Project.git 
    cd Clothing4413
    ```
    
    To run the project, you do not need to run the backend code, docker will do that. However for ease of access, cloning the repository gives you an easy way to navigate to the docker-compose.yml and frontend folder.
    
3. Start the backend through docker
   
    Make sure Docker Desktop is running, then in your terminal navigate to the Clothing4413 folder (where docker-compose.yml is) and run:

    docker-compose up

    This will automatically pull the backend and MySQL images from Docker Hub and start both services. No Java or Maven installation is required.

    Wait until you see the Spring Boot startup banner in the logs and the message "Database seeded" before proceeding.

 4. Start the frontend
    
    Open a new command terminal and navigate to where the frontend folder is located through:

    cd PROJECT_LOCATION/Clothing4413/frontend

    Install these dependencies before you start the project (Only for the first time running it)

    npm install
    npm install react-router-dom

    After the installation finishes, start the React Server using:

    npm start

6. Access in browser
   
    [http://localhost:3000](http://localhost:3000)

    On doing npm start it should automatically open the browser. However you can access the site through this link.

8. Accessing Users
   
    * To access Customer features either register a new customer and log in with those credentials, or log in with:
        * Email: John@example.com
        * Password: customer123
    * To access Admin features, log in with:
        * Email: Jane@admin.com
        * Password: admin123

## Notes
* The backend runs on port 8080 and the frontend runs on port 3000
* If your local MySQL is already running on port 3306, the Docker MySQL is mapped to port 3307 to avoid conflicts
* To fully stop the application run: 'docker-compose down'
* To reset the database to its initial state run: 'docker-compose down -v' then 'docker-compose up'

### Authors
- Timothy Tolstinev
- Justin Oguntala
- Eric Nguyen
- Eiad Sayed Suliman

EECS 4413 - Building E-Commerce Systems, York University
