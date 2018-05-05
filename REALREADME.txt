ASSETS
This folder stores .png files of any art assets you wish to use with the Level Editor at any given time.
These files can be multiple layers of folders deep for organization purposes, in which case they will
be sorted by their direct parent folder in the program.

KEYS
This stores .txt files acting as "legends" for the art assets to be used. Each line of the text file should
contain a "key" in the format <AssetName>-<character> (e.g. Couch-c)(Case sensitive, the AssetName is the name of the Image minus
.png). This will include the Asset in the list of used assets and assign the character to be used in the 2D level array to that
particular Asset (this of course means there should be no duplicate character's used, or multiple "keys" for one asset). You can
store as many "legends" as you want, the program will prompt you for the "legend" to use on startup.