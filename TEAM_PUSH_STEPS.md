# RailEase Team Push Steps

This file is for team members to push their assigned part of the project from their own laptop.

## Common setup for everyone

1. Download or clone the `RailEase` repository.
2. Copy the project files from the shared zip into the repo folder.
3. Open terminal in the project folder.
4. Run:

```powershell
git checkout main
git pull origin main
```

## Agrim

### Files to handle

- `.gitignore`
- `data/users.txt`
- `data/trains.txt`
- `data/tickets.txt`
- `src/models/Ticket.java`
- `src/models/Train.java`
- `src/models/User.java`
- `src/utils/FileUtil.java`

### Commands

```powershell
git checkout -b agrim-core
git add .gitignore data/users.txt data/trains.txt data/tickets.txt src/models/Ticket.java src/models/Train.java src/models/User.java src/utils/FileUtil.java
git commit -m "Build core models and file storage layer"
git push -u origin agrim-core
```

## Sara

### Files to handle

- `src/services/UserService.java`
- `src/services/TrainService.java`
- `src/services/TicketService.java`

### Commands

```powershell
git checkout main
git pull origin main
git checkout -b sara-services
git add src/services/UserService.java src/services/TrainService.java src/services/TicketService.java
git commit -m "Implement reservation services and booking workflow"
git push -u origin sara-services
```

## Aadita

### Files to handle

- `src/RailEaseApp.java`
- `src/ui/RailEaseUI.java`

### Commands

```powershell
git checkout main
git pull origin main
git checkout -b aadita-ui
git add src/RailEaseApp.java src/ui/RailEaseUI.java
git commit -m "Add desktop UI and connect app flow"
git push -u origin aadita-ui
```

## Merge order

1. `agrim-core`
2. `sara-services`
3. `aadita-ui`

## Important note

Even if the full project zip is copied into the repo, each member should only run `git add` for their assigned files.
