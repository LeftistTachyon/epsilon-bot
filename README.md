# ModularDiscordBot
A template GitHub repository for easily creating a Discord bot.  

## Usage  
To use this repo, create a new repository on GitHub with this repository as its template.  
Once that's been created, create a `Token` class with a `public static String getToken()` method that returns the token of your bot.  
To create methods, extend either `Command` or `Subcommand` or create a `ConsumerCommand` or `ConsumerSubcommand` object and supply the code to be executed. Make sure the created commands are added to the `TreeMap` in the `Commands` class.  
Once you run the `Main` class, the bot should be functional!
