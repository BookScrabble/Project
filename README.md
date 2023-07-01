s# Book Scrabble Game

This is a desktop application that implements the classic book Scrabble game using Java.


## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
  - [Server Side](#server-side)
  - [Client Side](#client-side)
- [Strategy](#strategy)
- [Credits](#credits)
- [Game Explanation](#game-explanation)

## Overview

Scrabble is a classic board game where players use letter tiles to create words on a game board. Each letter has a point value, and the goal is to create words with high point values. The game ends when one player has used all of their tiles, or when no more words can be created.

In this implementation, players take turns to place tiles on the board and create words. Each turn, the player can either place tiles on the board or exchange some of their tiles for new ones. The game ends when one player has used all of their tiles, or when no more words can be created.

In host mode, the game can accommodate up to four players, with at least one local player acting as the host. The other players can be either local or remote. In guest mode, the game can only accommodate one player who can connect to a host.


## Links
[Gameplay Video](https://youtu.be/F-Z9GDi880w)
JavaDocuments/view.bookscrabble/module-summary.html
[Javadoc](https://bookscrabble.github.io/BookScrabble/Project/JavaDocuments/view.bookscrabble/module-summary.html)


## Architecture

### <ins>Server-Side</ins>

The project utilizes a client-server architecture and makes use of design patterns, as well as generic collections and data structures for optimized runtime performance. The server-side algorithms implemented include LRU Cache, LFU Cache, and Bloom Filter to reduce redundant calculations. The project was designed according to SOLID principles, resulting in a well-structured and maintainable codebase.
<img src="https://github.com/BookScrabble/Project/blob/main/src/main/resources/Images/ServerSide.png"/>

### <ins>Client-Side</ins>
<img src="https://github.com/BookScrabble/Project/blob/main/src/main/resources/Images/ClientSide.png"/>




## Strategy

To ensure that the project is completed successfully and on time, we have established the following strategy:

- We will use Jira to manage the project and track progress.
- We will break the project into three milestones and plan weekly meetings to discuss progress and issues.
- We will use unit testing to ensure that the code is functioning as intended. 




## Credits

- [Liav Burger](https://github.com/LiavBurger)
- [Idan Asayag](https://github.com/idanasayag0)
- [Lior Hassin](https://github.com/liorhassin)



## Game Explanation
### <ins>Board</ins>

The game board is a 15x15 grid that features several bonus slots to help players increase their scores. These include:

- The central square, marked with a star, which doubles the value of any word played on it.
- Light blue squares, which double the value of any letter placed on them.
- Blue squares, which triple the value of any letter placed on them.
- Light red squares, which double the value of any word played on them.
- Red squares, which triple the value of any word played on them.
<img src="https://github.com/BookScrabble/Project/blob/main/src/main/resources/Images/board.png"/>


### <ins>Tiles</ins>

Each tile in the game has a corresponding point value, and the score of a word is calculated by adding up the point values of each tile in the word. 
The point value of each tile is based on the letter frequency in the English language and the difficulty of using that letter in a word. Common letters like "E" and "A" have a lower point value, while less common letters like "Q" and "Z" have a higher point value.

<img src="https://github.com/BookScrabble/Project/blob/main/src/main/resources/Images/Tiles.png" width="360" height="180"/>

## GANTT

<img src="https://github.com/BookScrabble/Project/blob/main/src/main/resources/Images/GANTT.png"  />







