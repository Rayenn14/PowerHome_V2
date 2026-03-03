# 🌿 Power Home - Gestion de Résidence Participative

Power Home est une application Android conçue pour aider les résidents d'une habitation collective à suivre et optimiser leur consommation électrique. Ce projet s'inscrit dans une démarche éco-citoyenne visant à réduire les pics de consommation par le biais de comportements responsables.

## 🎯 Objectifs du Projet
* **Optimisation Énergétique** : Suivi en temps réel de la consommation pour identifier et réduire les pics d'utilisation.
* **Sensibilisation Éco-citoyenne** : Encouragement à ne pas utiliser d'équipements énergivores durant les périodes critiques.
* **Gestion Participative** : Interface permettant aux résidents de gérer leurs équipements et leurs réservations de plages horaires.

## 🚀 Fonctionnalités Clés
* **Calendrier Interactif** : Analyse des pics de consommation sur quatre plages horaires quotidiennes (00h-06h, 06h-12h, 12h-18h, 18h-23h).
* **Code Couleur de Consommation** : Visualisation rapide du niveau de charge (Vert < 30%, Orange 30-70%, Rouge > 70%).
* **Plafonnement Intelligent** : Limite automatique fixée à 5000W entre 18h et 23h avec messages d'alerte en cas de dépassement.
* **Espace Résident** : Gestion personnalisée de l'habitat, ajout d'équipements électroménagers et profil utilisateur.
* **Authentification Sécurisée** : Système de connexion et d'inscription basé sur des tokens.

## 🛠 Stack Technique
* **Design & Conception** : Figma (Maquettage UI/UX avec courbes fluides et thème écologique).
* **Frontend** : Android Studio (Java & XML).
* **Backend** : PHP (Scripts d'interface serveur).
* **Base de Données** : MySQL (Gestion des utilisateurs, équipements, habitats et créneaux horaires).
* **Bibliothèque Tierce** : Koushik Dutta (pour les échanges de données Client-Serveur via requêtes HTTP).

## 🏗 Architecture de la Base de Données
Le backend repose sur une structure relationnelle optimisée :
* **Table User** : Stockage des informations utilisateurs et des tokens d'authentification.
* **Table Appliance** : Référentiel des équipements (puissance en Watts, références).
* **Table Habitat** : Liaison entre les utilisateurs et leurs espaces de vie.
* **Table Time_slot** : Gestion des créneaux de réservation et des limites de consommation par période.

## 📈 Méthodologie & Débogage
Le projet a été mené avec une approche rigoureuse :
* **Réunions régulières** pour la répartition des rôles et le suivi de l'avancement.
* **Outils de débogage** : Utilisation intensive de `Log.d()` pour identifier l'origine des erreurs entre le Java (Frontend), le PHP (API) et le SQL (Base de données).

---
*Projet réalisé par Rayen Bouzidi, Iris Njikam et Danya Belmokhtar dans le cadre du BUT Informatique à l'IUT Paris Rives de Seine.*
