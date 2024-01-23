# Environment
1. Open STS or Eclipse.
2. Import this project as Maven project.
3. After importing, the project should auto build and download dependency.
4. Right click on the project > Run As > Spring Boot App

# APIs
The base url is http://localhost:8080/restaurant
# 1. startSession (url: baseurl + "/startSession")
This API provided to create new session
# 2. joinSession (url: baseurl + "/joinSession")
This API provided to join existing session and return the list of submitted restaurant
# 3. endSession (url: baseurl + "/endSession")
This API provided to end the existing session with hostname. It will return the final selected restaurant name.
# 4. submitRestaurant (url: baseurl + "/submitRestaurant")
This API provided to accept new restaurant name submission.
# 5. getRestaurants (url: baseurl + "/getRestaurants)
This API provided to retrieve the submitted restaurant list
# 6. getSelectedRestaurant (url: baseurl + "/getSelectedRestaurant")
This API provided to retrieve the final selected restaurant name
