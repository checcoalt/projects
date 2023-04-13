# JSON document generator

attributeList = []      # list of fields
JSONdocument = ''       # document (initially empty)

# Fill the list of fields
while(True):
    field = input("Insert field name (0 to quit): ")
    if(field != "0"):
        attributeList.append(field)
    else:
        break

# Open the JSON document
JSONdocument += '[\n'

# Structure dimension
elements = int(input("How many elements do you want to insert? "))

for j in range(elements):

    # Open the object
    JSONdocument += '\t{\n'
    
    for i in range(len(attributeList)):
        # Input of the value for each field
        value = input("Insert value for the field '" + attributeList[i] + "' of the element " + str(j+1) +": ")
        JSONdocument += "\t\t\"" + attributeList[i] + "\":\"" + value

        # Close the field and add a comma if it's not the last one
        if i+1 < len(attributeList):
            JSONdocument += "\",\n"
        else:
            JSONdocument += "\"\n"

    # Close the object and add a comma if it's not the last one
    if j+1 < elements:
        JSONdocument += '\t},\n'
    else:
        JSONdocument += '\t}\n'

# Close the JSON document
JSONdocument += ']'

with open('file.json', 'w') as f:
    f.write(JSONdocument)
print(JSONdocument)