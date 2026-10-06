1. Description de l'application

Sujet : « Suivi de notes », application Android qui calcule la moyenne d'un étudiant et l'aide à se fixer un objectif.

Objectifs : saisir ses notes, calculer la moyenne, afficher la mention, définir un objectif, puis afficher un récapitulatif. Les données circulent d'une activité à l'autre par Bundle + Intent.

#	Activité	Rôle	Composants utilisés
1	MainActivity	Accueil : saisie du nom	ImageView, TextView, EditText, CheckBox, Button, Toast
2	NotesActivity	Saisie de 3 notes, calcul	LinearLayout contenant un RelativeLayout, EditText, Toast
3	ResultActivity	Moyenne et mention	ProgressBar, AlertDialog
4	ObjectifActivity	Choix de l'objectif	SeekBar, RadioButton, Switch
5	ResumeActivity	Récapitulatif	RelativeLayout dans LinearLayout, AlertDialog

Flux : Main → Notes → Result → Objectif → Resume. Le même Bundle est transmis d'une activité à l'autre et enrichi à chaque étape.

2. Rôle de chaque activité:

Activité 1 : MainActivity (écran d'accueil)
Utilité : identifier l'utilisateur et obtenir son accord avant de commencer.
Ce que fait l'utilisateur : saisit son nom, coche « J'accepte les conditions », appuie sur Commencer.
Contrôles de l'application : nom vide → Toast d'erreur ; case non cochée → Toast d'erreur. L'utilisateur ne passe à l'étape suivante que si les deux conditions sont remplies.
Donnée produite : nom.
Composants et leur raison : EditText pour saisir un texte libre, CheckBox pour un choix oui/non, Toast pour signaler une erreur sans bloquer l'écran.

Activité 2 : NotesActivity (saisie des notes)
Utilité : collecter les données nécessaires au calcul.
Ce que fait l'utilisateur : entre ses notes de Maths, Informatique et Anglais, puis appuie sur Calculer.
Contrôles de l'application : un champ vide → Toast ; note supérieure à 20 → Toast ; saisie invalide (ex. .) → Toast grâce au try/catch. L'application protège le calcul contre les données incorrectes.
Traitement (la logique métier) : moyenne = (n1 + n2 + n3) / 3.
Donnée produite : moyenne (ajoutée au Bundle qui contient déjà nom).
Ce qui montre le Bundle en action : le nom saisi à l'écran 1 s'affiche ici (« Bonjour Ines »), preuve que la donnée a bien voyagé.

Activité 3 : ResultActivity (résultat)
Utilité : présenter le résultat de façon lisible, pas seulement un nombre.
Ce que voit l'utilisateur : la moyenne en texte, la moyenne en barre de progression, la mention.
Traitement : conversion de la moyenne en mention (≥16 Très bien, ≥14 Bien, ≥12 Assez bien, ≥10 Passable, sinon Insuffisant).
Interaction : le bouton Voir les détails ouvre un AlertDialog qui résume nom, moyenne et mention. Le dialogue sert à afficher une information complémentaire sans changer d'écran.
Donnée produite : aucune nouvelle. Ce écran transmet le Bundle tel quel.
Composants et leur raison : ProgressBar pour visualiser la moyenne sur 20, AlertDialog pour une information secondaire.

Activité 4 : ObjectifActivity (objectif personnel)
Utilité : faire passer l'application d'un simple calcul à un outil de suivi : l'utilisateur se fixe un but.
Ce que fait l'utilisateur : règle un objectif de 0 à 20 avec le curseur, choisit son niveau (Débutant ou Avancé), active ou non les rappels.
Interaction en direct : le texte « Objectif : X » se met à jour pendant que le curseur bouge.
Données produites : objectif (entier), niveau (texte), rappel (vrai/faux).
Composants et leur raison : SeekBar pour choisir une valeur dans un intervalle, RadioButton pour un choix unique parmi plusieurs, Switch pour activer/désactiver une option.

Activité 5 : ResumeActivity (bilan)
Utilité : regrouper toutes les informations collectées et conclure.
Ce que voit l'utilisateur : nom, moyenne, objectif, niveau, état des rappels, et un message : « Objectif atteint » ou « Il manque X points ».
Traitement : comparaison moyenne >= objectif.
Interaction : le bouton Quitter demande confirmation (AlertDialog Oui/Non) avant de fermer l'application, pour éviter une fermeture accidentelle.
Ce que démontre cet écran : c'est la preuve que les données de toutes les activités précédentes sont arrivées, puisque l'écran lit 5 valeurs issues de 4 écrans différents.