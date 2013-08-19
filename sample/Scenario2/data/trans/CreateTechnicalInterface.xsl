<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.CreateTechnicalInterface.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:CreateTechnicalInterface xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<TechnicalInterface_VALUE>
						<technicalOperations>
							<name><xsl:value-of
								select="$SP1-REG.CreateTechnicalInterface.Req/soapenv:Envelope/soapenv:Body/q0:CreateTechnicalInterface/args1" /></name>
						</technicalOperations>
						<type><xsl:value-of
								select="$SP1-REG.CreateTechnicalInterface.Req/soapenv:Envelope/soapenv:Body/q0:CreateTechnicalInterface/args2" /></type>
					</TechnicalInterface_VALUE>
				</q1:CreateTechnicalInterface>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>