All work (code, design and art) is managed through Github.

### Help

In addition to doing normal work, consider helping to keep things moving and tidy. Try to keep the repository as a good centralized way of managing the project - if you make a big decision with someone about an issue you're working on, document it as a comment on the issue. Make sure there aren't tickets open for old, completed work. If you have an idea for a feature, write up an issue and flag it and tag people to discuss the idea. Keep your issues flagged correctly.

### Doing Work

- **To start an issue** pick an issue that doesn't have an "In Progress" tag assigned to it, and assign it to yourself, and apply the "In Progress" tag. If someone else is already assigned (but there is no "In Progress" tag), check with them to make sure they aren't already working on it.

- **When you finish an issue**, if it needs review flag it with "help wanted" and assign it to anyone who's input you'd like. Otherwise, if you're confident its done and done right, add a comment explaining where we can see what you've done and close the issue.

- **Creating issues** mostly isn't your problem. If you need to, please follow these guidelines:
    - Describe the nature of the issues as succinctly as possible in the title
    
    - Assign it to whoever is most likely responsible for fixing it, or Jeremy if you don't know who that is.
    
    - Describe the issue as comprehensively as possible. The person working the issue should be able to at least understand the issue fully without further input.
    
    - Issues should represent discrete units of work: "Make the website" is not a unit of work, "implement user account creation" is. If you have an issue with a dependency on another issue, link it.
    
**Flags**
- Art/Code/Design should be obvious.
- "help wanted" is for work in progress that need questions answered
- "In Progress" is for issues that are actively being developed
- "question" is for an idea for a feature/enhancement/change that hasn't been agreed upon yet, or for clarifying understanding of something not tied to a specific issue. These are for discussion.
- "bug" is for something (art, code or design) that you feel is incorrect and needs fixing.
- "feature" is for a thing that needs to be implemented that totally does not exist. (Creating a game)
- "enhancement" is for modifying a thing that does exist. (Adding behavior to a game)
    
### Asset Sharing (GDrive)

All our art assets are shared on Google Drive. If you need to talk about some non-code work, put it there and use the shareable URL to link it where necessary.

Try and keep the Google Drive as orderly as is reasonable. I'm trying to save all the mess created along the way because I like looking at change over time, but keep the mess in its designated folder.

### Source Control (branches)

Development takes place primarily on 3 branches,       all of these branches are for active development and breaking that build is a-okay. Just try not to leave it that way.

- html-Dev
    - This is where all our client-side code goes. For developing the HTML/CSS/JS side of the project. 
- minigame-Dev
    - This is where all of the client-side game development goes. LibGDX for transcompilation to JS. 
- server-dev
    - This is where the server-side code goes. Java EE running on Apache TomEE and postgresql.

We also currently have 2 other branches that you mostly shouldn't commit to:

- master
   - Mainline development branch, meant to represent a stable product for testing. This branch is automatically picked up by the buildserver. Don't break this branch. When your branch reaches a level of completion where you'd like its features to become part of the test product, submit a pull request from your branch to master (base master, compare: **X**-dev). Don't keep committing after you make that pull request, until it is approved. 
   - Small, global changes (to README.md or CONTRIBUTING.md for instance) can be committed directly to this branch, but apart from that, don't touch it.
  
   
- prod
   - Don't commit to this.  
   

### Targets

- Server:
    - OS: Ubuntu 14.04
    - Java EE 7 (Supports Java 8)
    - Application Server: Apache TomEE 7
    - Web server: Nginx
    - Database: Postgresql 9.5
    
- Games: Javascript/WebGL by way of transcompiled Java written against LibGDX. No fallback for older browsers.

- Client:
    - Support IE 8 when reasonable
    - All code must function on IE 11, Edge, Chrome, Firefox, Safari, Opera.
    - Angular 1.5
    - Typescript
    - Bootstrap (unmodified core, extended by CSS)
