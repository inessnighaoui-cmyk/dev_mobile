# Suivi de notes — Application Android

> **TP Développement mobile** — Intent, Bundle, listeners, layouts (5 activités)

---

## 1. Description de l'application

* **Sujet :** « Suivi de notes » calcule la moyenne d'un étudiant à partir de trois notes, affiche la mention, puis la compare à la moyenne du semestre précédent pour montrer son évolution.
* **Objectifs pédagogiques :**
  - Relier cinq activités par des `Intents` explicites.
  - Faire circuler des données avec un `Bundle`, dans les deux sens : à l'aller avec `startActivity`, au retour avec `startActivityForResult` / `onActivityResult`.
  - Réagir aux actions de l'utilisateur avec des écouteurs : `OnClickListener`, `OnSeekBarChangeListener`.
  - Combiner `LinearLayout`, `RelativeLayout` et `ScrollView`.

### Les cinq activités

| # | Activité | Rôle | Notions mises en œuvre |
|---|---|---|---|
| **1** | `MainActivity` | Accueil : nom et confirmation | `OnClickListener`, validation, `Toast`, création du `Bundle` |
| **2** | `NotesActivity` | Saisie de 3 notes, calcul de la moyenne | `ScrollView`, `LinearLayout`, `RelativeLayout`, `try/catch`, `Toast`, enrichissement du `Bundle` |
| **3** | `ResultActivity` | Moyenne, mention, réception des notes précédentes | `ProgressBar`, `AlertDialog`, `startActivityForResult`, `onActivityResult`, `onSaveInstanceState` |
| **4** | `PrecedentActivity` | Saisie des notes du semestre précédent | `SeekBar`, `OnSeekBarChangeListener`, `Switch`, `setResult`, `finish` |
| **5** | `EvolutionActivity` | Comparaison des deux semestres | `RelativeLayout` (en-tête), `ProgressBar`, `AlertDialog` |

### Flux de navigation

`Main` → `Notes` → `Result` → `Precedent` ↺ `Result` → `Evolution`

* `Result` lance `Precedent` et attend une réponse : `Precedent` renvoie les notes précédentes, que `Result` fusionne dans son `Bundle` avant d'ouvrir `Evolution`. Le bouton « Voir l'évolution » n'apparaît qu'une fois ces notes reçues.

### Rôle du Bundle

Un seul `Bundle` accompagne l'utilisateur d'un écran à l'autre et est enrichi à chaque étape. Les clés sont définies une seule fois, en constantes dans `MainActivity`.

| Étape | Sens | Clés ajoutées | Contenu du Bundle après l'étape |
|---|---|---|---|
| **Main → Notes** | Aller | `nom` | `nom` |
| **Notes → Result** | Aller | `moyenne`, `note_math`, `note_info`, `note_anglais` | `nom`, `moyenne`, 3 notes |
| **Result → Precedent** | Aller | Aucune | Idem (la moyenne sert de référence à l'écran) |
| **Precedent → Result** | Retour | `ancien_math`, `ancien_info`, `ancien_anglais`, `comparer` | Tout, plus les 3 anciennes notes et le choix du `Switch` |
| **Result → Evolution** | Aller | Aucune | Toutes les clés |

L'écran `Evolution` lit 8 valeurs issues de 2 écrans : moyenne et 3 notes actuelles (`Notes`), 3 anciennes notes et le choix `comparer` (`Precedent`). Le nom sert à `Notes` (salutation) et à `Result` (fenêtre de détails).

---

## 2. Rôle de chaque activité

### Activité 1 : `MainActivity`
* **Utilité :** identifier l'utilisateur et obtenir sa confirmation avant de commencer.
* **Contrôles :** nom vide → `Toast` ; case non cochée → `Toast`. Le passage à l'écran suivant n'a lieu que si les deux conditions sont remplies.
* **Donnée produite :** `nom`, placée dans un nouveau `Bundle`.

### Activité 2 : `NotesActivity`
* **Utilité :** collecter les trois notes et calculer la moyenne.
* **Contrôles (méthode `lireNote`, appelée pour chaque champ) :** champ vide, saisie non numérique (`try/catch` sur `NumberFormatException`) ou note hors de 0 à 20 → `Toast` qui nomme la matière fautive. La virgule décimale (`14,5`) est acceptée.
* **Traitement :** `moyenne = (n1 + n2 + n3) / 3`.
* **Données produites :** `moyenne` et les trois notes, ajoutées au `Bundle`.
* **Mise en page :** `ScrollView` (écran utilisable clavier ouvert) > `LinearLayout` > `RelativeLayout`, où chaque champ est placé sous le précédent avec `layout_below`.

### Activité 3 : `ResultActivity`
* **Utilité :** présenter le résultat et centraliser les données.
* **Traitement :** conversion de la moyenne en mention (≥ 16 Très bien, ≥ 14 Bien, ≥ 12 Assez bien, ≥ 10 Passable, sinon Insuffisant), affichée aussi dans une `ProgressBar` sur 20.
* **Interaction 1 :** un `AlertDialog` affiche nom, moyenne et mention sans changer d'écran.
* **Interaction 2 (aller-retour) :** « Saisir le semestre précédent » lance `Precedent` avec `startActivityForResult(intent, 101)`. Dans `onActivityResult`, on vérifie `requestCode` et `resultCode`, puis `putAll` fusionne les données reçues dans le `Bundle`. Le bouton « Voir l'évolution » devient visible.
* **Robustesse :** le `Bundle` est sauvegardé dans `onSaveInstanceState`, donc les données reçues survivent à une rotation d'écran. La touche Retour depuis `Precedent` (`RESULT_CANCELED`) ne modifie rien.

### Activité 4 : `PrecedentActivity`
* **Utilité :** saisir les trois notes du semestre précédent.
* **Reçoit :** le `Bundle` de l'aller, dont la moyenne actuelle affichée en référence ; s'il contient déjà des anciennes notes, les curseurs sont pré-remplis.
* **Interaction :** trois `SeekBar` (0 à 20) dont la valeur s'affiche en direct grâce à un `OnSeekBarChangeListener`. Un `Switch` « Comparer matière par matière » choisit le niveau de détail de l'écran suivant.
* **Renvoie (sans lancer d'autre écran) :** un `Bundle` de retour contenant les 3 anciennes notes et `comparer`, via `setResult(RESULT_OK, data)` puis `finish()`.

### Activité 5 : `EvolutionActivity`
* **Utilité :** comparer les deux semestres.
* **Traitement :** `ancienne moyenne = somme des 3 anciennes notes / 3` ; `écart = nouvelle − ancienne`. Le verdict (progression, recul ou stable) est décidé sur l'écart arrondi au centième, pour éviter les erreurs d'arrondi des nombres décimaux.
* **Affichage :** deux `ProgressBar` (ancienne et nouvelle moyenne), l'écart et le verdict. Si le `Switch` était actif, le détail par matière (ancienne note, nouvelle note, écart, flèche ↑ ↓ =) et la meilleure progression apparaissent.
* **Mise en page :** en-tête en `RelativeLayout` (titre collé à gauche avec `alignParentStart`, icône à droite avec `alignParentEnd`).
* **Interaction :** « Quitter » demande confirmation (`AlertDialog` Oui/Non) avant `finishAffinity()`.

---

## 3. Notions techniques mises en œuvre

| Notion | Rôle dans le projet |
|---|---|
| **Intent explicite** | Lance une activité précise à chaque changement d'écran |
| **Bundle** | Conteneur clé/valeur qui transporte les données ; clés centralisées en constantes |
| **startActivity** | Lancement sans réponse attendue (`Main` → `Notes` → `Result`, `Result` → `Evolution`) |
| **startActivityForResult / onActivityResult** | Lancement avec réponse identifiée par un `requestCode` (`Result` → `Precedent`) |
| **setResult + finish** | `Precedent` renvoie son `Bundle` à l'appelant et se ferme |
| **OnClickListener** | Réaction aux boutons (écrit en lambda) |
| **OnSeekBarChangeListener** | Affichage en direct de la valeur d'un curseur (classe anonyme, car l'interface a trois méthodes) |
| **LinearLayout, RelativeLayout, ScrollView** | Empilement vertical ; positionnement relatif (`layout_below`, `alignParent`...) ; défilement de l'écran |
| **Toast, AlertDialog** | Message d'erreur bref ; information complémentaire ou confirmation |
| **ProgressBar, SeekBar, Switch, CheckBox** | Visualisation d'une valeur sur 20 ; choix d'une valeur ; option oui/non |
| **onSaveInstanceState** | Conservation du `Bundle` lors de la rotation de l'écran |
| **strings.xml, AndroidManifest.xml** | Textes séparés du code ; déclaration des 5 activités |

---

## 4. Mécanisme aller-retour (extrait)

**ResultActivity (aller) :**
```java
Intent intent = new Intent(this, PrecedentActivity.class);
intent.putExtra(MainActivity.EXTRA_BUNDLE, donnees);
startActivityForResult(intent, REQUEST_PRECEDENT);
```

**PrecedentActivity (retour) :**
```java
Intent data = new Intent();
data.putExtras(retour);
setResult(RESULT_OK, data);
finish();
```

**ResultActivity (réception) :**
```java
if (requestCode == REQUEST_PRECEDENT && resultCode == RESULT_OK && data != null) {
    donnees.putAll(data.getExtras());
}
```

---

## 5. Choix de conception et limites

* **Anciennes notes :** saisies à la main, par valeurs entières (le curseur avance de 1 en 1) ; l'application ne peut pas vérifier leur exactitude.
* **Persistance :** aucune. Les données sont perdues à la fermeture de l'application, l'évolution compare donc deux saisies et non un historique.
* **Moyenne :** non pondérée (trois matières, mêmes coefficients).
* **API :** `startActivityForResult` est déprécié dans Android récent ; il est utilisé ici pour suivre le modèle vu en cours. Son remplaçant est `ActivityResultLauncher`.

---

## 6. Organisation du projet

* **5 classes Java :** `MainActivity`, `NotesActivity`, `ResultActivity`, `PrecedentActivity`, `EvolutionActivity`.
* **5 layouts :** `activity_main`, `activity_notes`, `activity_result`, `activity_precedent`, `activity_evolution`.
* **Textes dans :** `res/values/strings.xml`.
* **Manifest :** Les 5 activités sont déclarées dans `AndroidManifest.xml` ; `MainActivity` est l'activité de lancement. Le thème dérive de `Theme.AppCompat`.
