import csv

with open('champions.csv') as csv_file, open("champions_edit.csv", "w") as output:
    csv_file.readline()
    csv_reader = csv.reader(csv_file, delimiter=',')

    for row in csv_reader:
        if (len(row) == 11):
            row.append(row[1].replace(" ", "").replace(".", ""))

        output.write(",".join(row))
        output.write("\n")
