# Epsilon
A music trade bot for Discord! Made especially for **RD: The Sequel** group in DSiPaint.  

## Getting Started  
Clone this repository onto your computer. Right now, this bot is under development, so this project can only be logically run through IntelliJ.  
(You can, however, type the command `mvn exec:java` after you've compiled the code and finished the below steps as well, which will also work. Just make sure you do it in Command Prompt or Powershell and not in Git Bash or stopping the program will be a pain in the bum.)  
Once that's been done, create a `BOT_TOKEN` system environment variable, and set it to the bot token given to you by Discord's Developer Portal.  
Also, create a `ACCOUNT_KEY` system environment variable, and set it to the account key given to you by the Azure Storage account created to store information related to this bot.
Just run the `com.github.leftisttachyon.modulardiscordbot.Main` class and you're good to go!

### Exiting the bot  
If you're running in a console or something of the sort, you can exit by typing `q` and pressing `Enter`. It'll initiate an exit sequence that stores all data onto Azure for next time.  
Do not, I repeat, DO NOT, use `Ctrl + C` to exit this program if you want data related to this bot to be saved for later usage.  

## Features  
### What is this bot built for?  
Epsilon is mainly used for two things: song trades and album trades.
 - Song trades: suggesting songs for other people to listen to, secret-santa style. After you listen to the song, you rate it from one to ten and leave some thoughts if necessary.
 - Album trades: song trades, but less common and includes an entire album. You must have participated in a song trade beforehand in order to join an album trade.
### Bot features
Some of these features are under construction and will be denoted with `(WIP)`.  
 - Opting in and out of such trades
 - Adding extra information to your profile to help your giver to find a song that you would like `(WIP)`
 - Automagically automating every task for trades `(WIP)`
 - Periodic timing for trades, or manually starting them `(WIP)`
