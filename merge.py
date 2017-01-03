__author__ = 'Joseph Zhu'
import os,time,re,configparser,codecs,sys

current_dir=os.path.split(os.path.abspath(__file__))[0]
output_file = codecs.open(current_dir+'/'+'all.txt','w',encoding='utf-8')
files_list = os.listdir(current_dir)
for file_path in files_list:
    if '.txt' in file_path and file_path!='all.txt':
        print(file_path)
        lines = codecs.open(current_dir+'/'+file_path,encoding='utf-8',errors='ignore')
        for line in lines:
            output_file.write(line)
        lines.close

output_file.close()