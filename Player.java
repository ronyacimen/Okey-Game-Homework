import java.util.ArrayList;

public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        this.playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        this.numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile tile;
        
        if(this.playerTiles[index] != null){
            tile = this.playerTiles[index];
            this.playerTiles[index] = null;
            Tile[] newTiles = new Tile[playerTiles.length - 1]; // t0 t1 t2 t3 --> t0 null t2 t3 --> t0 t2 t3
            int j = 0;
            for(int i = 0; i < playerTiles.length - 1; i++){
                if(this.playerTiles[i] == null){
                    j++;
                }
                newTiles[i] = this.playerTiles[i + j];
            }

            this.numberOfTiles--;
            this.playerTiles = newTiles;
            
        }
        else{
            tile = null;
        }
        
        return tile;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {
        if(this.numberOfTiles > 14){
            System.out.println("You can not have more than 15 tiles in your hand.");
            return;
        }

        Tile[] newTiles = new Tile[this.numberOfTiles + 1];

        for(int i = 0; i < this.numberOfTiles; i++){
            newTiles[i] = this.playerTiles[i];
        }

        newTiles[newTiles.length - 1] = t;
        this.playerTiles = newTiles;
        this.numberOfTiles++;
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {
        int chainCount = 0;

        ArrayList<Tile> newTiles = new ArrayList<>();

        for(int i = 0; i < playerTiles.length; i++){
            newTiles.add(playerTiles[i]);
        }

        while (!newTiles.isEmpty()) { 
            ArrayList<Tile> chainTiles = new ArrayList<>();
            Tile firstTile = newTiles.get(0); 
            chainTiles.add(firstTile);
    
            for (int i = 1; i < newTiles.size(); i++) {
                Tile tile = newTiles.get(i);
                if (tile != null && firstTile.canFormChainWith(tile)) {
                    chainTiles.add(tile);
                    if (chainTiles.size() == 4) {
                        break; 
                    }
                }
            }
    
            if (chainTiles.size() == 4) {
                newTiles.removeAll(chainTiles); 
                chainCount++;
            } else {
                break; 
            }
        }

        return chainCount >= 3;
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles && playerTiles[i] != null; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
