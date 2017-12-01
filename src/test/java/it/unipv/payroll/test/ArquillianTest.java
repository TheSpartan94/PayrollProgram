package it.unipv.payroll.test;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class ArquillianTest {


	@Deployment(name = "Test")
	@OverProtocol("Servlet 3.0") 
	public static Archive<?> createDeployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "Test.war")
				.addPackages(true, "it.unipv.payroll.controller").addPackages(true, "it.unipv.payroll.DAO")
				.addPackages(true, "it.unipv.payroll.model").addPackages(true, "it.unipv.payroll.service")
				.addPackages(true, "it.unipv.payroll.util").addPackages(true, "it.unipv.payroll.test")
				.addPackages(true, "it.unipv.payroll.util").addAsResource("META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

		archive.as(ZipExporter.class).exportTo(new File("target/arquillianPackage.war"), true);

		return archive;

	}

}
