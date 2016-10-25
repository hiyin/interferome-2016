# interferome-2016
Web development on INTERFEROMEv2.1 to upgrade TF site analysis function by software integration

INTERFEROMEv2.1 Website: http://interferome.its.monash.edu.au/interferome/home.jspx
INTERFEROMEv2.0 Cite: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3531205/ 
CiiiDER: Java-based Transcription Factor Enrichment Analysis bioinformatics software (desktop/command line)
CiiiDER is aimed to empower existing transcription factor analysis with statistical analysis, triggered by clicking "TF Analysis" tab embedded in INTERFEROME top menu.

## How INTERFEROME works
### INTERFEROME is a online bioinformatics database/web app that has several components 
- database (data model)
- web user interface
- backend (Java)

### Web workflow: 
- User makes search request to interferome web app 
- retrieve data from interferome database based on request 
- Data being sent as input and analyzed by CiiiDER into statstical results 
- parse CiiiDER results in interferome backend 
- generate graphical results upon user query.

## Improvement in data processing/user search performance
Traditionally interferome parse CiiiDER's results in text files that are stored data locally (on server), which is inefficient as Java reading and writing text files takes time, later CiiiDER's input and results are moved to interferome local database, with modification in logic that handles this process, also implementing relevant classes (ORM by Hibernate) to direct query from database. 

This whole CiiiDER analysis involves use of gene, promoter and tfsite tables as CiiiDER requires them to be input to generate results (i.e. enriched transcription factor site), because gene table are updated timely (therefore promoter table), automating the updating of those tables then needs additional logic to serve. The upgrade work then separates to three iterations as below.

## Development Iteration
- Iteration 1 (Jul-Sept 2015): Integrate CiiiDER and Interferome
- Iteration 2 (Jan-Feb 2016): Optimising data processing/query for user search performance
- Iteration 3 (July-present 2016): Automating updating database and detailed code documentation (comments)

### Trello board (task tracker): https://trello.com/b/MYtni3w9/interferomev2-1-upgrade
### Note: 
- the board only contains records for Iteration 2 and 3
- for Iteration 1 records please refer to single document that contains complete log: see https://trello.com/c/nzp3tD2w/24-monash-interferome-upgrade-agenda attached pdf)

## Modification to Interferome
### classes have large change
- src/java/edu/monash/merc/struts2/action/SearchAction.java 
- src/java/edu/monash/merc/dao/impl/SearchDataDAO.java
- src/java/edu/monash/merc/system/scheduling/impl/INFDataProcessor.java

### classes have moderate change 
- src/java/edu/monash/merc/domain/TFSite.java
- src/java/edu/monash/merc/dao/impl/TfSiteDAO.java
- src/java/edu/monash/merc/service/impl/TFSiteServiceImpl.java
- src/java/edu/monash/merc/repository/ITFSiteRepository.java
- src/java/edu/monash/merc/service/impl/DMServiceImpl.java
- src/java/edu/monash/merc/service/DMService.java

### Classes created:
- src/java/edu/monash/merc/domain/Promoter.java 
- src/java/edu/monash/merc/dao/impl/PromoterDAO.java
- src/java/edu/monash/merc/service/PromoterService.java
- src/java/edu/monash/merc/service/impl/PromoterServiceImpl.java
- src/java/edu/monash/merc/repository/IPromoterRepository.java


