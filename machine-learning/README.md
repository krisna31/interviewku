# interviewku-machine-learning

## About the model & installation

- [Sentence Structure Model](#sentence-structure)
- [Scoring Model](#scoring)
- [Chatbot Model](#chatbot)
- [Dataset](https://docs.google.com/spreadsheets/d/1WbzJXW3zsgJh7GDSrEA7GAng60mC53gq/edit?usp=sharing&ouid=112356162045159196126&rtpof=true&sd=true)<br><br>
- [Windows Installation](#installation-for-windows)
- [linux Installation](#installation-for-linux)


## Sentence Structure
This model detects introductory sentence structures using Natural Language Processing (NLP). The input to this model is the user's response, and its output is a binary value of 0 or 1, indicating whether the response is considered "Good" or "Not Good".
<br>Example:
- "Saya biasanya membuat daftar prioritas untuk menentukan tindakan yang paling mendesak."<br>
-> Will give an output "Not Good"
- "Ketika saya dihadapkan pada masalah, saya biasanya membuat daftar prioritas untuk menentukan tindakan yang paling mendesak."<br>
-> Will give an output "Good"

## Scoring
The scoring result have a composition: 15% sentences structure, 5% retry attempt, 40% softmax classification, dan 40% similarity
- Softmax field classification by using the model created before
- Sentences similarity calculated by Cosine Similarity algorithm

#### Field Classification
This model performs classification of user responses into the fields present in the dataset. It utilizes Natural Language Processing (NLP) techniques, employing a softmax layer with the question fields as its classes (total of 16 classes).

#### Cosine Similarity
Cosine Similarity is a metric used to measure the similarity between two vectors. Specifically, this metric assesses similarity in direction or orientation of vectors, disregarding differences in their magnitudes or scales. Both vectors should be part of the same inner product space, meaning they should yield a scalar through inner product multiplication. The similarity between two vectors is measured by the cosine of the angle between them.

#### How to calculate Cosine Similarity
We define Cosine Similarity mathematically as the dot product of the vectors divided by the product of their magnitudes. For example, if we have two vectors, A and B, the similarity between them is calculated as:<br>
<img src="https://i.imgur.com/4vape0e.png" />
<br>Where:<br>
<img src="https://i.imgur.com/Y0Zp4KX.png" />

#### Implementation</h4>

Let say we have this 2 sentences
- D1 = 'the best data science course'
- D2 = 'data science is popular'

After creating a word table from the documents, the documents can be represented by the following vectors:<br>
<img src="https://i.imgur.com/VdgTsdA.png" />
    
- D1 = [1,1,1,1,1,0,0]
- D2 = [0,0,1,1,0,1,1]

Using these two vectors we can calculate cosine similarity. First, we calculate the dot product of the vectors:
<br>D1 . D2 = 1 x 0 + 1 x 0 + 1 x 1 + 1 x 1 + 1 x 0 + 0 x 1 + 0 x 1 = 2

Second, we calculate the magnitude of the vectors:<br>
<img src="https://i.imgur.com/Sp6MeY4.png" />

Finally, cosine similarity can be calculated by dividing the dot product by the magnitude<br>
<img src="https://i.imgur.com/ERPGdPg.png" />

The angle between the vectors is calculated as:<br>
<img src="https://i.imgur.com/e1Hn0Un.png" />
   
<p>So we get <b>0.44721</b> magnitude or <b>44.721%</b> that considered as Similarity Percentage between the two sentences, and vector angle is 63.435</p>

[Source](https://www.learndatasci.com/glossary/cosine-similarity/)

## Chatbot
The model is a chatbot specifically designed to address interview-related inquiries. It's constructed using a Natural Language Processing (NLP) with Recurrent Neural Network (RNN) using Gated Recurrent Unit (GRU) and Embedding techniques.
The embedding techniques using [GloVe algorithm](https://nlp.stanford.edu/projects/glove/) with indonesian dictionary word that scraped from Kaskus and Kompas, dictionary source: [here](https://github.com/ardwort/freq-dist-id).

<img src="https://imgur.com/Dneivz6.png" width='400px' />

## Tensorflow.js guide for Scoring and Sentence Structure model
### Installation for Windows
- Required NodeJS atleast v18.x
- Required Visual Studio with "Desktop Development with C++" Installed
- Open tf_install.bat
- Open tf_fix.bat
- Run the the js file

### Installation for Linux
- Required NodeJS atleast v18.x
- Required build-essential, run: `sudo apt-get install build-essential`
- Required node-pre-gyp installed globally, run: `npm i node-pre-gyp -g`
- Open tf_install.bat
- Open tf_fix.bat
- Run the the js file
