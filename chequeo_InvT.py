
import fileinput
import re


def InvTCheck(inv_t_log):
	cadena = (inv_t_log.readline(), 0)

	print(cadena)

	while(True):
		
		cadena = re.subn('(T1)(?=-)(.*?)(T2)(.*?)(T3)(.*?)|(T4)(.*?)(T5)(.*?)(T6)(.*?)|(T13)(.*?)(T14)(.*?)(T15)(.*?)|(T16)(.*?)(T17)(.*?)(T18)(.*?)',
		'\g<2>\g<4>\g<6>\g<8>\g<10>\g<12>\g<14>\g<16>\g<18>\g<20>\g<22>\g<24>', cadena[0].rstrip())

		print(cadena)

		if(cadena[1] == 0):
			break

	cadena = re.subn('-' , '' , cadena[0].rstrip())

	if(cadena[0] == ""):
		return("NO HAY FALLO DE T-INVARIANTES")
	else:
		return("FALLO EN T-INVARIANTES\n" + cadena[0])

inv_t_log = open("C:/Gaston/Facultad/PROGRAMACION CONCURRENTE/TpFinal/TP/data/log.txt" , "r")

print('\nResultado de chequeo en T-Invariantes: ' + InvTCheck(inv_t_log) + '\n')
