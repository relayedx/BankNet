- Prior to running the application you will need to have txt files that are stored locally on your machine to act as the database
- For simplicity name these txt files under "AllAccounts.txt" & "AllUsers.txt", no additional edits to the servers code will be needed.
- When cloning this repo the files are provided for you and you can then manually add Tellers under the "AllAccounts.txt" by following
the format for how the data is parsed being:
username|password|isTeller|authorizedAccts
- isTeller is just a place holder for "true" or "false" representing whether that user is a teller or not and if they do happen to be a teller
you can just have "0" be the place holder for authorizedAccts as all accounts have unique ID's that start at 1
- To use this application start the server by running the server.java file and upon a successful server being running clients can now connect!
- To connect from the client end, start the client.java file and make sure you either have the ip of the server that is running hard coded
or prompt for user input.
- Upon a successful connection a GUI will pop up being the login interface where both tellers and customers can log in
- After logging in each client will be redirected to their corresponding dashboard based on their role (Teller or Customer)
