import bs4
import os
import requests
import io
import hashlib
from bs4 import BeautifulSoup as soup
from urllib.request import urlopen, Request
import selenium
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from PIL import Image
import re
import urllib.request
from firebase import firebase
from selenium.webdriver.common.by import By 

# url used to get the exercise data
my_url = 'https://gethealthyu.com/exercise/?part=yogapose&type=flexibility&equipment=bodyweight#038;type=flexibility&equipment=bodyweight'
# path where the image files get saved
ORIGIN_PATH = 'C:\\Users\\costi\\Downloads\\webscrape'
# path where the Chrome driver needed to run the web scrapper is located
DATA_PATH = 'C:\Program Files (x86)\chromedriver.exe'
# setting the web driver for the web scrapper
wd = webdriver.Chrome(DATA_PATH)
# instantiating the database used for the application
firebase = firebase.FirebaseApplication('https://dumbbellapp-7ed5c-default-rtdb.europe-west1.firebasedatabase.app/', None)
# method used to download and export the images to .jpg format
def persist_image(folder_path:str,url:str):
    # try getting the image's url using requests
    try:
        image_content = requests.get(url).content
    # error exception if the image url could not be retrieved
    except Exception as e:
        print(f"ERROR - Could not download {url} - {e}")
    # if the image url was retrieved, try saving the file
    try:
        # getting the bytes of the image
        image_file = io.BytesIO(image_content)
        # convert bytes data to RGB
        image = Image.open(image_file).convert('RGB')
        # exporting image to '.jpg' format
        file_path = os.path.join(folder_path,hashlib.sha1(image_content).hexdigest()[:10] + '.jpg')
        with open(file_path, 'wb') as f:
            image.save(f, "JPEG", quality=85)
        print(f"SUCCESS - saved {url} - as {file_path}")
    # error exception if image could not be saved to file
    except Exception as e:
        print(f"ERROR - Could not save {url} - {e}")
# setting the website that the web driver will run
wd.get(my_url)
# getting the source code of the website
html = wd.page_source
# html parser
page_soup = soup(html, "html.parser")
# array used to store all the urls for the exercises' images
image_urls = []
# array used to store all the exercises' names to be stored in the database
names = []
# finding all the containers where each of the exercises' data is stored
containers = page_soup.findAll("article", {"class":"medium-4 cell"})
# find the images, using the 'img' tag
images = page_soup.findAll('img')
# find all the headers, containing the exercises' names, using the 'h1' tag
headers = page_soup.findAll('h1')
# getting all the images from the website, using the tag 'src'
for image in images:
       image_urls.append(image['src'])
# getting all the headers from the website
for header in headers:
        names.append(header.text[10:])
# getting all the images' links from the website
for x in range(len(image_urls)): 
    urllib.request.urlretrieve(image_urls[x], names[x+1] + ".jpg")
# saving the image files to computer
persist_image(ORIGIN_PATH,image_urls)

# Firebase upload section
# x holds the index of the 15 diferent containers showing exercises in the website
x = 1
# u holds the index used to order the exercises of the same kind (exercises/type/body_part/difficulty/order/name_of_exercise) in the database
u = 1
# e_type holds the type of the exercise (strength/cardio/flexibility), this gets set manually every time the script gets run
e_type = 'flexibility'
# e_body_part holds the body part that the exercise targets, this gets set manually every time the script gets run
e_body_part = 'total_body'

for y in names[:-1]:
    # setting the default difficulty to easy
    difficulty = "easy"
    # for the first five containers, set the difficulty to easy
    if(x<6):
        difficulty='easy'
    # for the next five exercises, set the difficulty to medium and reset the index number used to order the exercises of the same kind to 1
    if(x==6):
        u = 1
        difficulty = 'medium'
    if(x>6 and x<11):
        difficulty = 'medium'
    # for the last five exercises, set the difficulty to difficult and reset the index number used to order the exercises of the same kind to 1
    if(x==11):
        u = 1
        difficulty = 'difficult'
    if(x>11 and x<16):
        difficulty = 'difficult'
# saving the data to Firebase
    result = firebase.put('/exercises/'+ e_type + '/' + e_body_part + '/' + difficulty + '/' + str(u) +'/', 'name' , names[x])
    x = x + 1
    u = u + 1
# closing the program, once it has run all the code
wd.quit()